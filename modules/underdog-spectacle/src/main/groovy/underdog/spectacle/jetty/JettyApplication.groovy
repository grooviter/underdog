package underdog.spectacle.jetty

import groovy.transform.NamedParam
import groovy.transform.NamedVariant
import groovy.transform.TupleConstructor
import org.eclipse.jetty.server.Connector
import org.eclipse.jetty.server.Server
import org.eclipse.jetty.server.ServerConnector
import org.eclipse.jetty.server.handler.ContextHandler
import org.eclipse.jetty.server.handler.ContextHandlerCollection
import org.eclipse.jetty.server.handler.ResourceHandler
import org.eclipse.jetty.util.resource.ResourceFactory
import org.eclipse.jetty.util.thread.QueuedThreadPool
import org.eclipse.jetty.websocket.server.WebSocketUpgradeHandler
import underdog.spectacle.Application
import underdog.spectacle.dsl.HtmlApplication

import static java.lang.System.exit

/**
 * Default implementation of an Spectacle's {@link Application} using Jetty server
 *
 * @since 0.1.0
 */
@TupleConstructor
class JettyApplication implements Application {
    HtmlApplication htmlApplication
    Server server

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
        Connector connector = new ServerConnector(server)
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

        server.start()
        server.join()
    }

    private static WebSocketUpgradeHandler createWebSocketHandler(Server server) {
        return WebSocketUpgradeHandler.from(server) { container ->
            container.with {
                maxTextMessageSize = 128 * 1024
                addMapping("/status") { req, res, cb ->
                    return new DevHandler(req, res, cb)
                }
            }
        }
    }

    @Override
    @NamedVariant
    void dev(@NamedParam(required = false) File toWatch = new File("")) {
        DevWatcher.watcherBuilder()
            .dir(toWatch)
            .onWatch {
                this.setDevelopment()
                this.launch()
            }
            .onChange(this::stop)
            .build()
            .launch()
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
