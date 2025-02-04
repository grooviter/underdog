package underdog.guide.spectacle

import spock.lang.IgnoreIf
import spock.lang.Specification
import underdog.spectacle.Spectacle

@IgnoreIf({ System.getenv("OLLAMA_HOST") })
class CodeTranslatorSpec extends Specification {
    def "translate from java to groovy code"() {
        setup:
        def service = new CodeTranslatorService()

        and:
        def application = Spectacle.application {
            page("/translator", theme: "dark"){
                spec {
                    inputs {
                        textArea(name: 'java', label: "Java", info: "Code to translate", rows: 20)
                    }
                    outputs {
                        markdown(label: "Groovy", info: "Translated code")
                    }
                    onSubmit(service::translate)
                }
            }
        }

        expect:
        application.launch()
    }
}