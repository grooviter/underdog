package underdog.guide.plots

import spock.lang.Specification
import underdog.Underdog

class RadarSpec extends Specification {
    def "radar: simple"() {
        expect:
        // tag::simple[]
        Underdog
            .plots()
            .radar(
                ["power", "consumption", "price"], // <1>
                [200, 10, 100000],                 // <2>
                [150, 5, 54_350]                   // <3>
            ).show()
        // end::simple[]
    }
}
