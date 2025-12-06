package underdog.spectacle.dsl

import spock.lang.Specification

class UtilsSpec extends Specification{
    def "load configuration"() {
        setup:
        def configuration = Utils.loadConfiguration()

        expect:
        configuration.spectacle.training.rate == 0.75
        configuration.spectacle["title.poc"] == "Title of POC"
    }
}
