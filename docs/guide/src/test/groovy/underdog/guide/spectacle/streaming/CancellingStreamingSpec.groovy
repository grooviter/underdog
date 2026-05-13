package underdog.guide.spectacle.streaming

import reactor.core.publisher.Flux
import spock.lang.Specification
import underdog.spectacle.Spectacle
import underdog.spectacle.jetty.JettyWSContext

import static java.time.Duration.ofMillis

class CancellingStreamingSpec extends Specification {
    def "showing a simple component capable of triggering streaming over another component"() {
        setup:
        def application = Spectacle.application {
            // declaring context
            JettyWSContext cancellableContext
            // page DSL
            page("/cancellable-streaming", theme: "dark") {
                form(
                    streaming: true,
                    indicatorSelector: "#${field.submit}"
                ) {
                    card("card col-6") {
                        cardBody(className: "card-body d-flex flex-rows gap-1 justify-content-between") {
                            numberCard(name: field.counter, title: "Counter")
                            numberCard(name: field.derivative, title: "Derived Number")
                        }
                        cardFooter {
                            button(name: field.submit, text: "Start", iconName: "bi bi-play")
                            button(text: "Stop", iconName: "bi bi-stop") {
                                onClick([], [field.counter, field.derivative]) {
                                    // using context to cancel streaming
                                    cancellableContext.cancel()
                                    // after cancellation we'd like to reset certain values
                                    return [0, 0]
                                }
                            }
                        }
                    }
                    onSubmit([], [field.counter, field.derivative]) { JettyWSContext context ->
                        // setting cancellable context to use it later on
                        cancellableContext = context
                        // setting streaming values
                        Flux<Integer> mainFlux = Flux.fromArray(0..10 as Integer[]).delayElements(ofMillis(500))
                        Flux<Double> derivedFlux = mainFlux
                            .map { it * 0.23 }
                            .map { it.round(2).toDouble() }
                        // returning flux values to update targets
                        return [mainFlux, derivedFlux]
                    }
                }
            }
        }
        expect:
        application
        // application.launch()
    }
}
