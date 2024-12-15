package underdog.guide.plots

import spock.lang.Specification
import underdog.Underdog
import underdog.plots.Plots

class PieSpec extends Specification {
    def "pie: simple"() {
        expect:
        // --8<-- [start:simple]
        def plot = Underdog
            .plots()
            .pie(

                ('A'..'D'), // slice labels
                [9,5,6,4]   // slice values
            )
        plot.show()
        // --8<-- [end:simple]
        Plots.show(plot, theme: "dark")
    }

    def "pie: color mapping"() {
        expect:
        // --8<-- [start:color_mapping]
        // Colors matching the labels
        def COLORS = [
            "Red Bull": "#101864",
            "Ferrari": "#b03641",
            "Mclaren": "#d26f30",
            "Mercedes": "#505c62"
        ]

        def plot = Underdog.plots()
            .pie(
                // Labels
                ["Red Bull", "Ferrari", "Mclaren", "Mercedes"],
                // Values
                [9,5,6,4],
                // Passing color mappings
                colorMap: COLORS,
                title: "Top 4 Teams F1(tm) 2024 season",
                subtitle: "Total number of driver victories per team"
            )
        plot.show()
        // --8<-- [end:color_mapping]
        Plots.show(plot, theme: "dark")
    }

    def "pie: dataframe"() {
        expect:
        // --8<-- [start:dataframe]
        // source map
        def df = [
            names: ('A'..'D'),
            values: (10..40).by(10),
            colors: ['red', 'pink', 'yellow', 'lightblue']
        ].toDataFrame("dataframe")

        // passing dataframe to pie plot and show it
        def plot = Underdog.plots().pie(df)

        plot.show()
        // --8<-- [end:dataframe]
        Plots.show(plot, theme: "dark")
    }

    def "pie: series"() {
        expect:
        // --8<-- [start:series]
        // given a dataframe
        def df = [
                A: ('D'..'G'),
                B: (110..140).by(10),
                C: ['orange', 'gray', 'lightgray', 'blue']
        ].toDataFrame("dataframe")

        // we can pass series
        def plot = Underdog
            .plots()
            .pie(
                // using series "A" and renaming it to "names"
                df['A'].rename("names"),
                // using series "B" and renaming it to "values"
                df['B'].rename("values"),
                // using series "C" and renaming it to "colors"
                df['C'].rename("colors")
            )

        plot.show()
        // --8<-- [end:series]
        Plots.show(plot, theme: "dark")
    }
}
