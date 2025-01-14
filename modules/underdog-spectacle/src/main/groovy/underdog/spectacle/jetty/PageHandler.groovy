package underdog.spectacle.jetty

import org.eclipse.jetty.http.HttpHeader
import org.eclipse.jetty.http.HttpMethod
import org.eclipse.jetty.io.Content
import org.eclipse.jetty.server.Handler
import org.eclipse.jetty.server.Request
import org.eclipse.jetty.server.Response
import org.eclipse.jetty.server.handler.ConditionalHandler.ElseNext
import org.eclipse.jetty.util.Callback
import underdog.spectacle.dsl.HtmlPage
import underdog.spectacle.templates.TemplateEngine

class PageHandler extends ElseNext {
    HtmlPage page
    TemplateEngine templateEngine = new TemplateEngine()

    PageHandler(HtmlPage htmlPage) {
        this.page = htmlPage
        this.includeMethod(HttpMethod.GET.toString())
        this.includePath(htmlPage.path)
    }

    @Override
    List<Handler> getHandlers() {
        Handler next = this.getHandler();
        return next == null ? Collections.emptyList() : Collections.singletonList(next);
    }

    @Override
    protected boolean onConditionsMet(Request request, Response response, Callback callback) throws Exception {
        response.headers.add(HttpHeader.CONTENT_TYPE, "text/html")
        String result = templateEngine.render(this.page)
        Content.Sink.write(response, true, result, callback)
        return false
    }
}
