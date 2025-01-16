package underdog.spectacle.jetty

import groovy.transform.TupleConstructor
import org.eclipse.jetty.server.Connector
import org.eclipse.jetty.server.Server
import org.eclipse.jetty.server.ServerConnector
import org.eclipse.jetty.server.handler.ContextHandler
import org.eclipse.jetty.server.handler.ContextHandlerCollection
import org.eclipse.jetty.server.handler.ResourceHandler
import org.eclipse.jetty.util.resource.ResourceFactory
import underdog.spectacle.Application
import underdog.spectacle.dsl.HtmlApplication
import underdog.spectacle.dsl.HtmlEvent
import underdog.spectacle.dsl.HtmlPage

/**
 * Default implementation of an Spectacle's {@link Application} using Jetty server
 *
 * @since 0.1.0
 */
@TupleConstructor
class JettyApplication implements Application {
    HtmlApplication htmlApplication

    @Override
    void launch() {
        List<BackendHandler> backendHandlerList = htmlApplication
            .eventList
            .<HtmlEvent, BackendHandler>collect {
                new BackendHandler(it, htmlApplication)
            }

        List<PageHandler> pageHandlerList = htmlApplication
            .pageList
            .<HtmlPage, PageHandler>collect {
                new PageHandler(it)
            }

        Server server = new Server(5000)
        Connector connector = new ServerConnector(server)
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

        server.setHandler(contextHandlerCollection)

        server.start()
        server.join()
    }
}
