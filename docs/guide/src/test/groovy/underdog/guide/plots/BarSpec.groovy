package underdog.guide.plots

import underdog.Underdog
import underdog.plots.Plots
import spock.lang.Specification

class BarSpec extends Specification {
    def "simple"() {
        expect:
        // --8<-- [start:simple]
        Plots.plots()
            .bar(
                1..12,
                [10, 12, 18, 3, 0, 20, 10, 12, 18, 3, 0, 10],
                title: "Using bars",
                xLabel: "Months",
                yLabel: "Indicator"
            )
            .show()
        // --8<-- [end:simple]
    }

    def "histogram"() {
        expect:
        // --8<-- [start:histogram_simple]
        // generating data
        def random = new Random()
        def distribution = (0..1_000).collect { random.nextGaussian(0, 50) }

        // plot
        Underdog
            .plots()
            .histogram(
                distribution,
                title: "Distribution",
                subtitle: "mean: 0 / stddev: 50",
                bins: 10
            ).show()
        // --8<-- [end:histogram_simple]
    }
}
