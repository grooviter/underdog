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
import underdog.spectacle.dsl.HtmlEvent

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
    static final Integer DEV_BROWSER_OPEN_TIMEOUT = 10_000

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
        log.debug("launching application")
        log.debug("loading backend handlers")
        List<BackendHandler> backendHandlerList = htmlApplication
            .eventList
            .findAll(HtmlEvent::isNotStreaming)
            .collect {
                new BackendHandler(it, htmlApplication)
            }

        log.debug("loading streaming events")
        List<HtmlEvent> streamingEvents = htmlApplication
            .eventList
            .findAll(HtmlEvent::isStreaming)

        log.debug("loading page handlers")
        List<PageHandler> pageHandlerList = htmlApplication
            .pageList
            .collect {
                new PageHandler(it)
            }

        log.debug("creating server instance")
        this.server = new Server(new QueuedThreadPool(10))
        connector = new ServerConnector(server)
        connector.setPort(5000)
        server.addConnector(connector)

        ContextHandlerCollection contextHandlerCollection = new ContextHandlerCollection()

        log.debug("adding backend handlers")
        // REST API
        backendHandlerList.each {
            contextHandlerCollection.addHandler(new ContextHandler(it, "/"))
        }

        log.debug("adding websocket handlers")
        // WS API
        streamingEvents.each {
            WebSocketUpgradeHandler handler = new WebSocketUpgradeHandlerBuilder()
                .event(it)
                .application(htmlApplication)
                .server(server)
                .build()

            contextHandlerCollection.addHandler(new ContextHandler(handler, "/"))
        }

        log.debug("adding page handlers")
        // PAGES
        pageHandlerList.each {
            contextHandlerCollection.addHandler(new ContextHandler(it, "/"))
        }

        log.debug("adding static resources")
        // STATIC RESOURCES
        ResourceHandler resourceHandler = new ResourceHandler()
        resourceHandler.setBaseResource(ResourceFactory.of(resourceHandler).newResource(this.class.getResource('/static')))
        resourceHandler.setDirAllowed(true)
        resourceHandler.setAcceptRanges(true)
        contextHandlerCollection.addHandler(new ContextHandler(resourceHandler, '/static'))

        log.debug("checking dev mode")
        // WS DEV MODE
        if (isDevelopment()) {
            log.debug("adding dev handler")
            contextHandlerCollection.addHandler(new ContextHandler(createWebSocketHandler(server), "/ws"))
        }

        log.debug("adding root handler")
        server.setHandler(contextHandlerCollection)

        log.debug("checking startup listener")
        if (this.startupListener) {
            log.debug("adding startup listener")
            this.server.addEventListener(this.startupListener)
        }

        log.debug("starting server")
        server.start()

        log.debug("joining server")
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
