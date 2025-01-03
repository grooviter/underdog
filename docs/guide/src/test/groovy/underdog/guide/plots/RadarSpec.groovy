package underdog.guide.plots

import spock.lang.Specification
import underdog.Underdog
import underdog.plots.Plots

class RadarSpec extends Specification {
    def "radar: simple"() {
        expect:
        // --8<-- [start:simple]
        def plot = Underdog
            .plots()
            .radar(
                ["power", "consumption", "price"], // Name of the categories
                [200, 10, 100000],                 // Maximum values for each category
                [150, 5, 54_350]                   // Actual value for each category
            )
        plot.show()
        // --8<-- [end:simple]
        Plots.show(plot, theme: "dark")
    }
}
