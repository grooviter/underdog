package underdog.spectacle.jetty

import org.eclipse.jetty.server.Handler
import org.eclipse.jetty.server.handler.ResourceHandler
import org.eclipse.jetty.util.resource.ResourceFactory
import underdog.spectacle.dsl.HtmlPage

class PageHandler extends ResourceHandler {
    HtmlPage page

    PageHandler(HtmlPage htmlPage) {
        this.setDirAllowed(false)
        this.setAcceptRanges(true)
        this.setBaseResource(ResourceFactory.of(this).newResource("/"))
    }

    @Override
    List<Handler> getHandlers() {
        Handler next = this.getHandler();
        return next == null ? Collections.emptyList() : Collections.singletonList(next);
    }
}
