package underdog.guide.spectacle

import spock.lang.Specification
import underdog.spectacle.Spectacle
import underdog.spectacle.dsl.HtmlApplication
import underdog.spectacle.dsl.HtmlNavigation
import underdog.spectacle.jetty.JettyApplication

class GroupsSpec extends Specification {
    def "application pages can be arranged in groups"() {
        when:
        def app = Spectacle.application(theme: 'dark') {
            group(name: 'images', icon : 'bi bi-image', title: 'Images')

            page(
                '/about',
                title: "About",
                icon: 'bi bi-clock'
            ) {
                markdown "About"
            }
            page(
                '/images/config',
                title: "Configuration",
                group: 'images',
                icon: 'bi bi-gear'
            ) {
                markdown "Configuration"
            }
            page(
                '/images/generation',
                title: "Generations",
                group: 'images',
                    icon: 'bi bi-image'
            ) {
                markdown "Generations"
            }
            page(
                '/images/edits',
                title: "Edits",
                group: 'images',
                icon: 'bi bi-pencil'
            ) {
                markdown "Edits"
            }
        } as JettyApplication

        and:
        HtmlApplication htmlApplication = app.htmlApplication
        HtmlNavigation htmlNavigation = htmlApplication.applicationNavigation

        then:
        htmlNavigation.topElements.size() == 2
        htmlNavigation.groups.size() == 1
        htmlNavigation.groups.images.included.size() == 3
    }
}
