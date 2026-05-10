package underdog.guide.spectacle.streaming

import reactor.core.publisher.Flux
import spock.lang.Specification
import underdog.spectacle.Spectacle
import underdog.spectacle.jetty.JettyWSContext

import static java.time.Duration.ofSeconds

class CancellingStreamingSpec extends Specification {
    def "showing a simple component capable of triggering streaming over another component"() {
        setup:
        def application = Spectacle.application {
            // declaring context
            JettyWSContext cancellableContext

            page("/cancellable-streaming", theme: "dark") {
                form(
                    streaming: true,
                    indicatorSelector: "#${field.submit}"
                ) {
                    card("card col-6") {
                        cardBody {
                            numberCard(name: field.counter)
                        }
                        cardFooter {
                            button(name: field.submit, text: "Start", iconName: "bi bi-play")
                            button(text: "Stop", iconName: "bi bi-stop") {
                                onClick([], [field.counter]) {
                                    // using context to cancel streaming
                                    cancellableContext.cancelContext()
                                    return 0
                                }
                            }
                        }
                    }
                    onSubmit([], [field.counter]) { JettyWSContext context ->
                        // setting cancellable context to use it later on
                        cancellableContext = context
                        return Flux.fromArray(0..1000 as Integer[])
                            .delayElements(ofSeconds(1))
                            .map(Object::toString)
                    }
                }
            }
        }
        expect:
        application
        // application.launch()
    }
}
