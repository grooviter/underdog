package underdog.spectacle.jetty

import groovy.transform.TupleConstructor
import groovy.util.logging.Slf4j
import org.eclipse.jetty.util.Callback
import org.eclipse.jetty.websocket.api.Session
import org.eclipse.jetty.websocket.api.Session.Listener.AbstractAutoDemanding
import org.eclipse.jetty.websocket.server.ServerUpgradeRequest
import org.eclipse.jetty.websocket.server.ServerUpgradeResponse
import reactor.core.publisher.Flux
import underdog.spectacle.dsl.HtmlApplication
import underdog.spectacle.dsl.HtmlElementWithValue
import underdog.spectacle.dsl.HtmlEvent
import underdog.spectacle.templates.CachedTemplateEngine

import java.time.Duration

/**
 * TODO: missing Flux cancellation (onMessage -> maybe just dispose the current and execute next ?)
 * https://www.baeldung.com/spring-webflux-cancel-flux
 *
 * @since 0.1.0
 */
@Slf4j
@TupleConstructor(includes = ['request', 'response', 'callback', 'application', 'event', 'templateEngine'])
class StreamingHandler extends AbstractAutoDemanding {
    ServerUpgradeRequest request
    ServerUpgradeResponse response
    HtmlApplication application
    HtmlEvent event
    Session session
    Callback callback
    CachedTemplateEngine templateEngine

    @Override
    void onWebSocketOpen(Session session) {
        this.session = session
        this.session.setIdleTimeout(Duration.ofSeconds(120))
        log.debug("websocket connection open")
    }

    private sendText(String text) {
        this.session.sendText(text, null)
    }

    private String executeTemplate(Object targetValue) {
        def target = this.event
            .outputList
            .<String, HtmlElementWithValue>collect(this.application::findHtmlElementWithValueByName)
            .find()
            .tap { it.value = targetValue }
        return templateEngine.render(target)
    }

    @Override
    void onWebSocketText(String message) {
        def function = this.event.function
        def context = new JettyWSContext(message, application)
        Flux flux = function(context) as Flux
        flux
            .map(this::executeTemplate)
            .doOnError(this::println)
            .subscribe(this::sendText)
    }

    @Override
    void onWebSocketClose(int statusCode, String reason) {
        this.session.close()
    }
}
