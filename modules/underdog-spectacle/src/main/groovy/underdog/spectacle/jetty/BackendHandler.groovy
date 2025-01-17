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
import underdog.spectacle.templates.TemplateEngine

import static org.eclipse.jetty.http.HttpHeader.CONTENT_TYPE

/**
 * Represents the functionality executed by a backend service. It will execute a given element event and will
 * return html fragments.
 *
 * @since 0.1.0
 */
class BackendHandler extends ElseNext {
    HtmlEvent event
    HtmlApplication application
    TemplateEngine templateEngine = new TemplateEngine()

    /**
     * Creates a new {@link BackendHandler}
     *
     * @param event the element associated with this event execution
     * @param application the application associated with this execution
     * @since 0.1.0
     */
    BackendHandler(
        HtmlEvent event,
        HtmlApplication application
    ) {
        super()
        this.event = event
        this.application = application
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
        def context = new JettyContext(request, response, this.application.configuration)
        def targetValue = function(context)
        def target = this.event
            .outputList
            .<String, HtmlElementWithValue>collect(this.application::findHtmlElementWithValueByName)
            .find()
            .tap { it.value = targetValue }

        response.headers.add(CONTENT_TYPE, "text/html")
        Content.Sink.write(response, true, templateEngine.render(target), callback)
        return true
    }
}
