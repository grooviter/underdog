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
import underdog.spectacle.dsl.Controller
import underdog.spectacle.dsl.HtmlApplication
import underdog.spectacle.dsl.HtmlPage

@TupleConstructor
class JettyApplication implements Application {
    HtmlApplication htmlApplication

    @Override
    void launch() {
        List<RESTHandler> restHandlerList = htmlApplication.controllerList.<Controller, RESTHandler>collect {
            new POSTHandler(it) // TODO: resolve handler type by method type
        }
        List<PageHandler> pageHandlerList = htmlApplication.htmlPageList.<HtmlPage, PageHandler>collect {
            new PageHandler(it)
        }

        Server server = new Server(5000)
        Connector connector = new ServerConnector(server)
        server.addConnector(connector)

        ContextHandlerCollection contextHandlerCollection = new ContextHandlerCollection()

        // REST API
        restHandlerList.each {
            contextHandlerCollection.addHandler(new ContextHandler(it, "/api"))
        }

        // PAGES
        pageHandlerList.each {
            contextHandlerCollection.addHandler(new ContextHandler(it, "/"))
        }

        // STATIC RESOURCES
        ResourceHandler resourceHandler = new ResourceHandler()
        resourceHandler.setBaseResource(ResourceFactory.of(resourceHandler).newResource(this.class.getResource('/css')))
        resourceHandler.setDirAllowed(true)
        resourceHandler.setAcceptRanges(true)
        contextHandlerCollection.addHandler(new ContextHandler(resourceHandler, '/css'))

        server.setHandler(contextHandlerCollection)

        server.start()
        server.join()
    }
}
