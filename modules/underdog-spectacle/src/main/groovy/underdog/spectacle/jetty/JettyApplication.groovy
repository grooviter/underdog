package underdog.spectacle.jetty

import groovy.transform.NamedParam
import groovy.transform.NamedVariant
import groovy.transform.TupleConstructor
import groovy.util.logging.Slf4j
import org.eclipse.jetty.server.Server
import org.eclipse.jetty.server.ServerConnector
import org.eclipse.jetty.server.handler.ContextHandler
import org.eclipse.jetty.server.handler.ContextHandlerCollection
import org.eclipse.jetty.server.handler.ResourceHandler
import org.eclipse.jetty.util.component.LifeCycle
import org.eclipse.jetty.util.resource.ResourceFactory
import org.eclipse.jetty.util.thread.QueuedThreadPool
import org.eclipse.jetty.websocket.server.WebSocketUpgradeHandler
import underdog.spectacle.Application
import underdog.spectacle.dsl.HtmlApplication

import java.util.concurrent.Executors

import static java.awt.Desktop.desktop
import static java.lang.System.exit

/**
 * Default implementation of an Spectacle's {@link Application} using Jetty server
 *
 * @since 0.1.0
 */
@Slf4j
@TupleConstructor(includes = ['htmlApplication', 'server'])
class JettyApplication implements Application {
    /**
     *
     * How long the process must wait before deciding whether to open the browser or not
     *
     * @since 0.1.0
     */
    static final Integer DEV_BROWSER_OPEN_TIMEOUT = 5000

    /**
     * Size limit of the websocket messages
     *
     * @since 0.1.0
     */
    static final Integer DEV_WS_TEXT_MESSAGE_SIZE = 128 * 1024

    /**
     * URL path of the websocket endpoint
     *
     * @since 0.1.0
     */
    static final String DEV_WS_ENDPOINT_PATH = "/status"

    /**
     * Instance of {@link HtmlApplication} we would like to render
     *
     * @since 0.1.0
     */
    HtmlApplication htmlApplication

    /**
     * Underlying infrastructure implementation
     *
     * @since 0.1.0
     */
    Server server

    /**
     * Listener triggered once the application starts
     *
     * @since 0.1.0
     */
    LifeCycle.Listener startupListener

    /**
     * Underlying infrastructure implementation
     *
     * @since 0.1.0
     */
    ServerConnector connector

    @Override
    void launch() {
        List<BackendHandler> backendHandlerList = htmlApplication
            .eventList
            .collect {
                new BackendHandler(it, htmlApplication)
            }

        List<PageHandler> pageHandlerList = htmlApplication
            .pageList
            .collect {
                new PageHandler(it)
            }

        this.server = new Server(new QueuedThreadPool(10))
        connector = new ServerConnector(server)
        connector.setPort(5000)
        server.addConnector(connector)

        ContextHandlerCollection contextHandlerCollection = new ContextHandlerCollection()

        // REST API
        backendHandlerList.each {
            contextHandlerCollection.addHandler(new ContextHandler(it, "/"))
        }

        // PAGES
        pageHandlerList.each {
            contextHandlerCollection.addHandler(new ContextHandler(it, "/"))
        }

        // STATIC RESOURCES
        ResourceHandler resourceHandler = new ResourceHandler()
        resourceHandler.setBaseResource(ResourceFactory.of(resourceHandler).newResource(this.class.getResource('/static')))
        resourceHandler.setDirAllowed(true)
        resourceHandler.setAcceptRanges(true)
        contextHandlerCollection.addHandler(new ContextHandler(resourceHandler, '/static'))

        // WS
        if (isDevelopment()) {
            contextHandlerCollection.addHandler(new ContextHandler(createWebSocketHandler(server), "/ws"))
        }

        server.setHandler(contextHandlerCollection)

        if (this.startupListener) {
            this.server.addEventListener(this.startupListener)
        }

        server.start()
        server.join()
    }

    private static WebSocketUpgradeHandler createWebSocketHandler(Server server) {
        return WebSocketUpgradeHandler.from(server) { container ->
            container.with {
                maxTextMessageSize = DEV_WS_TEXT_MESSAGE_SIZE
                addMapping(DEV_WS_ENDPOINT_PATH) { req, res, cb ->
                    return new DevHandler(req, res, cb)
                }
            }
        }
    }

    @Override
    @NamedVariant
    void dev(@NamedParam(required = false) File toWatch = new File("")) {
        log.debug("running spectacle in development mode")
        DevWatcher.watcherBuilder()
            .dir(toWatch)
            .onWatch(this::launchDevelopment)
            .onChange(this::stop)
            .build()
            .launch()
    }

    private void launchDevelopment() {
        this.setDevelopment()
        this.startupListener = new LifeCycle.Listener() {
            void lifeCycleStarted(LifeCycle event) {
                Executors.newSingleThreadExecutor().execute {
                    Thread.sleep(DEV_BROWSER_OPEN_TIMEOUT)
                    int browserClients = DevHandler.clientsConnected.intValue()
                    if (!browserClients) {
                        String browserURI = "http://localhost:${connector.port}${htmlApplication.defaultPath}"
                        log.debug("opening spectacle at $browserURI")
                        desktop.browse(URI.create(browserURI))
                    } else {
                        log.debug("skipping opening browser, clients already connected (${browserClients})")
                    }
                }
            }
        }
        this.launch()
    }

    private boolean isDevelopment() {
        this.htmlApplication.isDevelopment()
    }

    private void setDevelopment() {
        this.htmlApplication.setDevelopment(true)
    }

    @Override
    void stop() {
        this.server.stop()
        exit(0)
    }
}
