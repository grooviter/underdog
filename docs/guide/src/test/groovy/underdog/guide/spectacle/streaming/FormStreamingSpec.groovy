package underdog.guide.spectacle.streaming

import reactor.core.publisher.Flux
import spock.lang.Specification
import underdog.spectacle.Spectacle

import java.time.Duration

class FormStreamingSpec extends Specification {
    def "create a simple form which streams values to output fields"() {
        setup:
        def application = Spectacle.application {
            // --8<-- [start:form_streaming]
            page("/streaming-form") {
                def updatedNumber = text(label: "Updated number", editable: false)
                form(streaming: true){
                    onSubmit([], [updatedNumber.name]) {
                        return Flux.fromArray(0..10 as Integer[])
                            .delayElements(Duration.ofSeconds(1))
                            .repeat()
                    }
                    button(text: "Update field")
                }
            }
            // --8<-- [end:form_streaming]
        }
        expect:
        application
//        application.launch()
    }
}
