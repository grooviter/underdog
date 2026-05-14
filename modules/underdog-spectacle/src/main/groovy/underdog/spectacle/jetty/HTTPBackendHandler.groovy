package underdog.spectacle.jetty

import org.eclipse.jetty.http.HttpMethod
import org.eclipse.jetty.io.Content
import org.eclipse.jetty.server.Handler
import org.eclipse.jetty.server.Request
import org.eclipse.jetty.server.Response
import org.eclipse.jetty.server.handler.ConditionalHandler.ElseNext
import org.eclipse.jetty.util.Callback
import underdog.spectacle.dsl.HtmlApplication
import underdog.spectacle.dsl.HtmlElementWithValue
import underdog.spectacle.dsl.HtmlEvent
import underdog.spectacle.templates.CachedTemplateEngine

import static org.eclipse.jetty.http.HttpHeader.CONTENT_TYPE

/**
 * Represents the functionality executed by a backend service. It will execute a given element event and will
 * return html fragments.
 *
 * @since 0.1.0
 */
class HTTPBackendHandler extends ElseNext {
    HtmlEvent event
    HtmlApplication application
    CachedTemplateEngine templateEngine

    /**
     * Creates a new {@link HTTPBackendHandler}
     *
     * @param event the element associated with this event execution
     * @param application the application associated with this execution
     * @since 0.1.0
     */
    HTTPBackendHandler(
        HtmlEvent event,
        HtmlApplication application,
        CachedTemplateEngine templateEngine
    ) {
        super()
        this.event = event
        this.application = application
        this.templateEngine = templateEngine
        this.includeMethod(HttpMethod.POST.toString())
        this.includePath(event.path)
    }

    @Override
    List<Handler> getHandlers() {
        Handler next = this.getHandler();
        return next == null ? Collections.<Handler>emptyList() : Collections.singletonList(next);
    }

    @Override
    protected boolean onConditionsMet(Request request, Response response, Callback callback) throws Exception {
        def function = this.event.function
        def context = new JettyHTTPContext(request, this.application)
        def targetValues = [function(context)].flatten()

        def targetList = this.event
            .outputList
            .<String, HtmlElementWithValue>collect(this.application::findHtmlElementWithValueByName)

        response.headers.add(CONTENT_TYPE, "text/html")

        if (targetValues?.size() != targetList?.size()) {
            throw new RuntimeException("""\
              | The number of values is different than the number of targets.
              | This usually happens when an HTMLElement tries to receive a collection as a value.
            """.stripIndent().stripMargin())
        }

        String combined = ""
        for (int i = 0; i < targetValues.size(); i++) {
            def target = targetList[i]
            def value = targetValues[i]
            target.value = value
            combined += templateEngine.render(target)
        }

        Content.Sink.write(response, true, combined, callback)
        return true
    }
}
