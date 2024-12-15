package underdog.guide.plots

import underdog.Underdog
import underdog.plots.Plots
import spock.lang.Specification

class ScatterSpec extends Specification {
    def "simple"() {
        expect:
        // --8<-- [start:simple]
        // numbers from 0 to 99
        // You can use a "range or a list" for X axis
        def xs = 0..<100

        // 100 random numbers
        // You can use a "range or a list" for the Y axis
        def ys = (0..<100).collect { new Random().nextInt(100) }

        // plot
        def plot = Underdog.plots()
            .scatter(
                xs,
                ys,
                title: "Random Numbers") // Optional attributes

        plot.show()
        // --8<-- [end:simple]
        Plots.show(plot, theme: "dark")
    }

    def "simple series"() {
        setup:
        def df = [
            xs: 0..<100,
            ys: (0..<100).collect { new Random().nextInt(100) }
        ].toDF("numbers")
        expect:
        // --8<-- [start:simple_series]
        Plots.plots()
                .scatter(
                    // using a series for x axis and renaming it to X
                    df['xs'].rename('X'),
                    // using another series for y axis and renaming it to Y
                    df['ys'].rename('Y'),
                    title: "Random Numbers")
                .show()
        // --8<-- [end:simple_series]
    }
}
