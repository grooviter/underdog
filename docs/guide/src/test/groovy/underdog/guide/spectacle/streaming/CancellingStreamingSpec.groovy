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
            // page DSL
            page(
            "/cancellable-streaming",
                theme: "dark"
            ) {
                form(
                    streaming: true,
                    indicatorSelector: "#${field.submit}"
                ) {
                    row {
                        col {
                            card("card") {
                                cardBody(className: "card-body d-flex flex-rows gap-1 justify-content-between") {
                                    div("w-100 d-flex justify-content-between gap-1") {
                                        numberCard(name: field.counter, title: "Counter Number")
                                        numberCard(name: field.derivative, title: "Derived Number")
                                    }
                                }
                                cardFooter {
                                    div("d-flex justify-content-between") {
                                        button(name: field.submit, text: "Start", iconName: "bi bi-play")
                                        button(text: "Stop", iconName: "bi bi-stop") {
                                            onClick([], [field.counter, field.derivative, field.timeLine]) {
                                                // using context to cancel streaming
                                                cancellableContext.cancel()
                                                // after cancellation we'd like to reset certain values
                                                return [0, 0, new HtmlTimeLineItems()]
                                            }
                                        }
                                    }
                                }
                            }
                            onSubmit([], [field.counter, field.derivative, field.timeLine]) { JettyWSContext context ->
                                // setting cancellable context to use it later on
                                cancellableContext = context
                                // setting streaming values
                                Flux<Integer> mainFlux = Flux.fromArray(1..10 as Integer[]).delayElements(ofMillis(500))
                                Flux<Double> derivedFlux = mainFlux
                                        .map { it * 0.23 }
                                        .map { it.round(2).toDouble() }
                                Flux<HtmlTimeLineItems> timeLineFlux = mainFlux.share()
                                        .map(CancellingStreamingSpec::evaluateLevels)
                                        .distinct()
                                        .scan(new HtmlTimeLineItems(), CancellingStreamingSpec::evaluateTimeLineItems)

                                // returning flux values to update targets
                                return [mainFlux, derivedFlux, timeLineFlux]
                            }
                        }
                        col("col-6 d-flex justify-content-center align-items-center") {
                            timeLine(field.timeLine)
                        }
                    }
                }
            }
        }
        expect:
        application
        application.launch()
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
        switch (level) {
            case "A":
                state.items << new HtmlTimeLineItem(
                    name: "TIMELINE_NAME",
                    title: "STEP",
                    description: "You are in level 1",
                    iconText: "1",
                    iconBackground: "bg-success"
                )
                break
            case "B":
                state.items << new HtmlTimeLineItem(
                    name: "TIMELINE_NAME",
                    title: "STEP",
                    description: "You are in level 2",
                    iconText: "2",
                    iconBackground: "bg-warning"
                )
                break
            default:
                state.items << new HtmlTimeLineItem(
                    name: "TIMELINE_NAME",
                    title: "STEP",
                    description: "You are in level 3",
                    iconText: "3",
                    iconBackground: "bg-danger"
                )
                break
        }
        return state
    }
}
