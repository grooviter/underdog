package underdog.guide.spectacle.streaming

import reactor.core.publisher.Flux
import spock.lang.Specification
import underdog.spectacle.Spectacle
import underdog.spectacle.dsl.components.HtmlTimeLine.HtmlTimeLineItems
import underdog.spectacle.dsl.components.HtmlTimeLineItem
import underdog.spectacle.jetty.JettyWSContext

import static java.time.Duration.ofMillis

class CancellingStreamingSpec extends Specification {
    def "showing a simple component capable of triggering streaming over another component"() {
        setup:
        def application = Spectacle.application {
            // declaring context
            JettyWSContext cancellableContext
            List outputs = [field.counter, field.doubleCounter, field.derivative, field.timeLine]
            // page DSL
            page("/cancellable-streaming", theme: "dark") {
                form(streaming: true, indicatorSelector: "#${field.submit}") {
                    row {
                        col("col-6") {
                            card("card") {
                                cardHeader(
                                    "Cancellable Flux",
                                    "Example on how to use Project Reactor with Spectacle"
                                )
                                cardBody(className: "card-body d-flex flex-rows gap-1 justify-content-between") {
                                    col("col-3 d-flex flex-column justify-content-between gap-2") {
                                        numberCard(name: field.counter, title: "Source Number")
                                        numberCard(name: field.doubleCounter, title: "Double Number")
                                        numberCard(name: field.derivative, title: "Derived Number")
                                    }
                                    col("col-9 d-flex justify-content-center align-items-center") {
                                        timeLine(field.timeLine)
                                    }
                                }
                                cardFooter {
                                    div("d-flex justify-content-between") {
                                        button(name: field.submit, text: "Start", iconName: "bi bi-play")
                                        button(text: "Stop", iconName: "bi bi-stop") {
                                            onClick([], outputs) {
                                                // using context to cancel streaming
                                                cancellableContext.cancel()
                                                // after cancellation we'd like to reset certain values
                                                return [0, 0, 0, new HtmlTimeLineItems()]
                                            }
                                        }
                                    }
                                }
                            }
                            onSubmit([], outputs) { JettyWSContext context ->
                                // setting cancellable context to use it later on
                                cancellableContext = context
                                // setting streaming values
                                Flux<Integer> mainFlux = Flux.fromArray(1..10 as Integer[]).delayElements(ofMillis(500))
                                Flux<Integer> doubleFlux = mainFlux.map { it * 2 }
                                Flux<Double> derivedFlux = mainFlux
                                    .map { it * 0.23 }
                                    .map { it.round(2).toDouble() }
                                Flux<HtmlTimeLineItems> timeLineFlux = mainFlux.share()
                                    .map(CancellingStreamingSpec::evaluateLevels)
                                    .distinct()
                                    .scan(new HtmlTimeLineItems(), CancellingStreamingSpec::evaluateTimeLineItems)
                                // returning flux values to update targets
                                return [mainFlux, doubleFlux, derivedFlux, timeLineFlux]
                            }
                        }
                    }
                }
            }
        }
        expect:
        application
        // application.launch()
    }

    static String evaluateLevels(Integer current) {
        if (current < 3) {
            return "A"
        } else if (current >= 3 && current <= 8) {
            return "B"
        } else {
            return "C"
        }
    }

    static HtmlTimeLineItems evaluateTimeLineItems(HtmlTimeLineItems state, String level) {
        String iconBackground = switch(level) {
            case "A" -> "bg-success"
            case "B" -> "bg-warning"
            default -> "bg-danger"
        }
        state.items << new HtmlTimeLineItem(
            name: "TIMELINE_NAME",
            title: "STEP",
            description: "You are in level ${level}",
            iconText: level,
            iconBackground: iconBackground
        )
        return state
    }
}
