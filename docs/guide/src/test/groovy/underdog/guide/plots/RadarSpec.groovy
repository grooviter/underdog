package underdog.guide.plots

import spock.lang.Specification
import underdog.Underdog

class RadarSpec extends Specification {
    def "radar: simple"() {
        expect:
        // --8<-- [start:simple]
        Underdog
            .plots()
            .radar(
                ["power", "consumption", "price"], // Name of the categories
                [200, 10, 100000],                 // Maximum values for each category
                [150, 5, 54_350]                   // Actual value for each category
            ).show()
        // --8<-- [end:simple]
    }
}
