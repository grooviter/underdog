package underdog.guide.spectacle

import spock.lang.Specification
import underdog.Underdog
import underdog.spectacle.Spectacle
import underdog.spectacle.dsl.Context

class SpecSpec extends Specification {
    void 'simple spec'() {
        setup:
        // --8<-- [start:example]
        def app = Spectacle.application {
            page('/poc'){
                spec {
                    inputs {
                        text(label: "Name", info: "Name of the experiment")
                        number(label: 'Timeout', info: 'How long must the system wait (sec) until cancelling the experiment')
                    }
                    outputs {
                        dataframe(label: 'Experiment result')
                    }
                    examples = [
                        [name: 'Experiment 1', timeout: 20],
                        [name: 'Experiment 2', timeout: 40],
                    ]
                }
            }
        }
        // --8<-- [end:example]
        expect:
        app.launch()
    }

    void 'simple spec complete'() {
        setup:
        // --8<-- [start:example_complete]
        def app = Spectacle.application {
            page('/poc'){
                spec {
                    inputs {
                        text(
                            name: field.name,
                            label: "Name",
                            info: "Name of the experiment")
                        number(
                            name: field.timeout,
                            label: 'Timeout',
                            info: 'How long must the system wait (sec) until cancelling the experiment')
                    }
                    outputs {
                        dataframe(label: 'Experiment result')
                    }
                    examples = [
                        [name: 'Experiment 1', timeout: 20],
                        [name: 'Experiment 2', timeout: 40],
                    ]
                    onSubmit { Context context ->
                        def timeout = context.pInteger(field.timeout)
                        def name = context.param(field.name)
                        println("experiment ${name} must not last more than ${timeout} seconds")
                        return Underdog.df().empty(name)
                    }
                }
            }
        }
        // --8<-- [end:example_complete]
        expect:
        app
        //app.launch()
    }
}
