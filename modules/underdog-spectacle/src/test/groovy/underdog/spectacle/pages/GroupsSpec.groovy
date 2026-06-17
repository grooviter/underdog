package underdog.spectacle.pages

import spock.lang.Specification
import underdog.spectacle.Spectacle
import underdog.spectacle.dsl.HtmlApplication
import underdog.spectacle.dsl.HtmlNavigation
import underdog.spectacle.dsl.HtmlPage
import underdog.spectacle.jetty.JettyApplication

import java.util.function.Function

class GroupsSpec extends Specification {

    static interface PageProvider extends Function<HtmlApplication, HtmlPage> {}

    static class ConfigurationPageProvider implements PageProvider {
        @Override
        HtmlPage apply(HtmlApplication app) {
            return HtmlPage.create(path: '/images/config', title: 'Configuration', icon: 'bi bi-gear', application: app) {
                markdown "Configuration"
            }
        }
    }

    static class ImageGenerationPageProvider implements PageProvider {
        @Override
        HtmlPage apply(HtmlApplication app) {
            return HtmlPage.create(path: '/images/txt2img', title: 'txt2img', icon: 'bi bi-image', application: app) {
                markdown "Image Generation"
            }
        }
    }

    static class ImageEditPageProvider implements PageProvider {
        @Override
        HtmlPage apply(HtmlApplication app) {
            return HtmlPage.create(path: '/images/img2img', title: 'img2img', icon : 'bi bi-image', application:  app) {
                markdown "Image Edit"
            }
        }
    }

    def "application pages can be arranged in groups"() {
        when:
        def app = Spectacle.application(theme: 'dark') {
            def images = group(name: 'images', icon : 'bi bi-image', title: 'Images')

            page(
                '/about',
                title: "About",
                icon: 'bi bi-clock'
            ) {
                markdown "About"
            }

            page(images, new ConfigurationPageProvider())
            page(images, new ImageGenerationPageProvider())
            page(images, new ImageEditPageProvider())

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
