package underdog.guide.spectacle.streaming

import reactor.core.publisher.Flux
import spock.lang.Specification
import underdog.spectacle.Spectacle
import underdog.spectacle.dsl.Context
import underdog.spectacle.dsl.components.HtmlNumberCard

import java.time.Duration

class FormStreamingSpec extends Specification {
    static Duration EVERY_SECOND = Duration.ofSeconds(1)
    static String RESPONSIVE_CSS = "col-sm-12 col-md-3 col-lg-2"
    def "create a simple form which streams values to output fields"() {
        setup:
        def application = Spectacle.application {
            // --8<-- [start:form_streaming]
            page("/streaming-form", title: 'Countdown') {
                form(
                    streaming: true,
                    indicatorSelector: "button",
                    disabledSelector: "select"
                ){
                    row {
                        col(RESPONSIVE_CSS) {
                            select(label: "Seconds", name: application.field.selectedSeconds) {
                                (10..40).step(10).each {
                                    option("$it", "$it seconds")
                                }
                            }
                        }
                        col(RESPONSIVE_CSS) {
                            select(label: "Users", name: application.field.selectedUsers) {
                                (100..400).step(100).each {
                                    option("$it", "$it users")
                                }
                            }
                        }
                        col(RESPONSIVE_CSS) {
                            select(label: "Requests/sec", name: application.field.selectedRequests) {
                                (1000..4000).step(1000).each {
                                    option("$it", "$it req/sec")
                                }
                            }
                        }
                    }
                    row {
                        col(RESPONSIVE_CSS) {
                            numberCard(name: application.field.countdown, title: "Countdown", symbol: 'secs')
                        }
                        col(RESPONSIVE_CSS) {
                            numberCard(name: application.field.users, title: "Users", symbol: 'users')
                        }
                        col(RESPONSIVE_CSS) {
                            numberCard(name: application.field.requestsPerSecond, title: "Requests/Sec", symbol: 'req')
                        }
                    }
                    row {
                        col(RESPONSIVE_CSS) {
                            button(text: "Update field", className: "+mt-3")
                        }

                        onSubmit(
                            [
                                application.field.selectedSeconds,
                                application.field.selectedUsers,
                                application.field.selectedRequests,
                            ],
                            [
                                application.field.countdown,
                                application.field.users,
                                application.field.requestsPerSecond
                            ]
                        ) { Context ctx ->
                            Integer maxUsers = ctx.pInteger(application.field.selectedUsers)
                            Integer maxRequests = ctx.pInteger(application.field.selectedRequests)

                            Flux<Integer> seconds = Flux
                                .fromArray(ctx.pInteger(application.field.selectedSeconds)..0 as Integer[])
                                .delayElements(EVERY_SECOND)

                            return [
                                seconds.map(n -> {
                                    return new HtmlNumberCard.Value(value: n, delta: n)
                                }),
                                seconds.map(n -> {
                                    return new HtmlNumberCard.Value(value: new Random().nextInt(maxUsers), delta: n)
                                }),
                                seconds.map(n -> {
                                    return new HtmlNumberCard.Value(value: new Random().nextInt(maxRequests), delta: n)
                                }),
                            ]

                        }
                    }
                }
            }
            // --8<-- [end:form_streaming]
        }
        expect:
        application
        // application.launch()
    }
}
