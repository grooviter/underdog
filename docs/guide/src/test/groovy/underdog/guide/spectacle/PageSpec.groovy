package underdog.guide.spectacle

import spock.lang.Specification
import underdog.spectacle.Spectacle
import underdog.spectacle.dsl.HtmlApplication
import underdog.spectacle.dsl.HtmlPage

import java.util.function.Function

class PageSpec extends Specification {
    // --8<-- [start:page_refactor_class]
    static class AboutPage implements Function<HtmlApplication, HtmlPage>{
        private static final PATH = "/about"

        // You could use DI to inject some service
        // @Inject
        // SomeService service

        @Override
        HtmlPage apply(HtmlApplication htmlApplication) {
            return HtmlPage.create(path: PATH, application: htmlApplication){
                markdown """\
                  | # Refactoring pages
                  |
                  | We can split the page build outside the application DSL
                """
                button(text: "Send positive feedback") {
                    onClick([], []){
                        // service.doSomething()
                    }
                }
            }
        }
    }
    // --8<-- [end:page_refactor_class]

    def "using pre build page instance"() {
        setup:
        def aboutPageClassInstance = new AboutPage()
        // --8<-- [start:page_refactor]
        def application = Spectacle.application {
            page(this::buildFormMethod)
            page(aboutPageClassInstance::apply)
        }
        // --8<-- [end:page_refactor]
        expect:
        application.launch()
    }

    // --8<-- [start:page_refactor_method]
    HtmlPage buildFormMethod(HtmlApplication app) {
        return HtmlPage.create(path: '/form', application: app) {
            row {
                col(className: 'col-4') {
                    text(label: "Data", placeHolder: 'some data here')
                    button(text: "send")
                }
            }
        }
    }
    // --8<-- [end:page_refactor_method]
}
