package underdog.spectacle


import spock.lang.Specification
import underdog.spectacle.pages.AboutPageProvider
import underdog.spectacle.pages.ChartUpdatePageProvider
import underdog.spectacle.pages.MultipleOutputUpdatesPageProvider
import underdog.spectacle.pages.StreamingUpdatePageHandler

class SpectacleSpec extends Specification {
    def "creating a complete application"() {
        setup:
        def application = Spectacle.application {
            page(new ChartUpdatePageProvider())
            page(new MultipleOutputUpdatesPageProvider())
            page(new AboutPageProvider())
            page(new StreamingUpdatePageHandler())
        }
        // application.launch()

        expect:
        application
    }
}
