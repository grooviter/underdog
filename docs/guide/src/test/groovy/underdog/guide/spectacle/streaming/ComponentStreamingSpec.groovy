package underdog.guide.spectacle.streaming

import reactor.core.publisher.Flux
import spock.lang.Specification
import underdog.spectacle.Spectacle
import underdog.spectacle.dsl.Context

import static java.time.Duration.ofSeconds

class ComponentStreamingSpec extends Specification {
    def "showing a simple component capable of triggering streaming over another component"() {
        setup:
        def application = Spectacle.application {
            // --8<-- [start:component_streaming]
            page("/streaming-component") {
                def markdownField = markdown()
                button("Update") {
                    onClick([], [markdownField.name], true) { Context context ->
                        return Flux.fromArray(0..10 as Integer[])
                            .delayElements(ofSeconds(1))
                            .map(Object::toString)
                            .repeat()
                    }
                }
            }
            // --8<-- [end:component_streaming]
        }
        expect:
        application
        //application.launch()
    }
}
