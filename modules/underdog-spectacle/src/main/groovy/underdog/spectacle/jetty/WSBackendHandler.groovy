package underdog.spectacle.jetty

import groovy.transform.TupleConstructor
import groovy.util.logging.Slf4j
import org.eclipse.jetty.util.Callback
import org.eclipse.jetty.websocket.api.Session
import org.eclipse.jetty.websocket.api.Session.Listener.AbstractAutoDemanding
import org.eclipse.jetty.websocket.server.ServerUpgradeRequest
import org.eclipse.jetty.websocket.server.ServerUpgradeResponse
import reactor.core.Disposable
import reactor.core.publisher.Flux
import underdog.spectacle.dsl.HtmlApplication
import underdog.spectacle.dsl.HtmlElementWithValue
import underdog.spectacle.dsl.HtmlEvent
import underdog.spectacle.templates.CachedTemplateEngine

import java.time.Duration
import java.util.concurrent.atomic.AtomicReference

/**
 * Handlers of this type will handle websocket communication between client and server
 *
 * @since 0.1.0
 */
@Slf4j
@TupleConstructor(includes = ['request', 'response', 'callback', 'application', 'event', 'templateEngine'])
class WSBackendHandler extends AbstractAutoDemanding {
    ServerUpgradeRequest request
    ServerUpgradeResponse response
    HtmlApplication application
    HtmlEvent event
    Session session
    Callback callback
    CachedTemplateEngine templateEngine
    Disposable disposable

    @Override
    void onWebSocketOpen(Session session) {
        this.session = session
        this.session.setIdleTimeout(Duration.ofSeconds(120))
        log.debug("websocket connection open")
    }

    private sendText(String text) {
        this.session.sendText(text, null)
    }

    @Override
    void onWebSocketText(String message) {
        def function = this.event.function
        def context = new JettyWSContext(message, application)

        def targetValues = [function(context)].flatten() as List<Flux>
        def targetList = this.event
            .outputList
            .<String, HtmlElementWithValue>collect(this.application::findHtmlElementWithValueByName)

        List<Flux<String>> fluxes = [targetValues, targetList].transpose().collect { Flux flux, HtmlElementWithValue target ->
            flux.map { value ->
                target.value = value
                templateEngine.render(target)
            }
        }

        AtomicReference<Disposable> ref = new AtomicReference<>()
        this.disposable = Flux.merge(fluxes)
            .doOnError(log::error)
            .subscribe { html ->
                // Inspired in https://www.baeldung.com/spring-webflux-cancel-flux
                if (context.isCancelled()) {
                    ref.get().dispose()
                    this.session.disconnect()
                    log.debug("websocket context cancelled")
                    return
                }
                sendText(html)
            }
        ref.set(this.disposable)
    }

    @Override
    void onWebSocketClose(int statusCode, String reason) {
        this.session.close()
        this.disposable?.dispose()
    }
}
