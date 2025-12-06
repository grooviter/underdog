package underdog.guide.spectacle.webscrapping

import spock.lang.IgnoreIf
import spock.lang.Specification
import underdog.spectacle.Spectacle

class WebScrappingSpec extends Specification {
    @IgnoreIf({ !System.getenv("OLLAMA_HOST") })
    def "scrapping using Ollama"() {
        setup:
        def summaryService = new SummaryService(new ScrappingService(), new AIService())

        and:
        def application = Spectacle.application {
            page("/scrapping") {
                markdown """\
                | # Spectacle WebScrapping
                |
                | This experiment will make use of JSoup and Ollama to
                | summarize web pages
                """
                spec {
                    inputs {
                        text(name: 'pageURL', placeHolder: "https://...", required: true)
                    }
                    outputs {
                        markdown()
                    }
                    onSubmit(summaryService::apply)
                }
            }
        }

        expect:
        application
        // application.launch()
    }
}
