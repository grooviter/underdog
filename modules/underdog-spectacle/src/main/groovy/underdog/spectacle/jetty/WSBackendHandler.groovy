package underdog.spectacle.jetty

import groovy.transform.TupleConstructor
import groovy.util.logging.Slf4j
import org.eclipse.jetty.util.Callback
import org.eclipse.jetty.websocket.api.Session
import org.eclipse.jetty.websocket.api.Session.Listener.AbstractAutoDemanding
import org.eclipse.jetty.websocket.server.ServerUpgradeRequest
import org.eclipse.jetty.websocket.server.ServerUpgradeResponse
import reactor.core.publisher.Flux
import reactor.core.publisher.Mono
import underdog.spectacle.dsl.HtmlApplication
import underdog.spectacle.dsl.HtmlElement
import underdog.spectacle.dsl.HtmlEvent
import underdog.spectacle.templates.CachedTemplateEngine

import java.time.Duration

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

    @Override
    void onWebSocketOpen(Session session) {
        this.session = session
        this.session.setIdleTimeout(Duration.ofSeconds(120))
        log.debug("websocket connection open")
    }

    private static Mono<Void> sendTextAsync(Session session, String msg) {
        return Mono.create { sink ->
            session.sendText(msg, new org.eclipse.jetty.websocket.api.Callback() {
                @Override
                void succeed() {
                    sink.success()
                }

                @Override
                void fail(Throwable x) {
                    sink.error(x)
                }
            })
        }
    }

    @Override
    void onWebSocketText(String message) {
        def function = this.event.function
        def context = new JettyWSContext(message, application)

        def targetValues = [function(context)].flatten() as List<Flux>
        def targetList = this.event
            .outputList
            .<String, HtmlElement>collect(this.application::findHtmlElementWithValueByName)

        List<Flux<String>> fluxes = [targetValues, targetList].transpose().collect { Flux flux, HtmlElement target ->
            flux.map { value ->
                target.value = value
                templateEngine.render(target)
            }
        }

        Flux.merge(fluxes)
            .onBackpressureLatest()
            .concatMap { msg -> sendTextAsync(this.session, msg) }
            .doFinally { this.session.disconnect() }
            .doOnError { log.error(it.message, it) }
            .takeUntilOther(context.cancelMono())
            .subscribe()
    }

    @Override
    void onWebSocketClose(int statusCode, String reason) {
        this.session.close()
    }
}
