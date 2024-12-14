package underdog.guide.plots

import spock.lang.Specification
import underdog.Underdog

class PieSpec extends Specification {
    def "pie: simple"() {
        expect:
        // --8<-- [start:simple]
        Underdog
            .plots()
            .pie(

                ('A'..'D'), // slice labels
                [9,5,6,4]   // slice values
            ).show()
        // --8<-- [end:simple]
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

        Underdog.plots()
            .pie(
                // Labels
                ["Red Bull", "Ferrari", "Mclaren", "Mercedes"],
                // Values
                [9,5,6,4],
                // Passing color mappings
                colorMap: COLORS,
                title: "Top 4 Teams F1(tm) 2024 season",
                subtitle: "Total number of driver victories per team"
            ).show()
        // --8<-- [end:color_mapping]
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
        Underdog.plots().pie(df).show()
        // --8<-- [end:dataframe]
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
        Underdog
            .plots()
            .pie(
                // using series "A" and renaming it to "names"
                df['A'].rename("names"),
                // using series "B" and renaming it to "values"
                df['B'].rename("values"),
                // using series "C" and renaming it to "colors"
                df['C'].rename("colors")
            )
            .show()
        // --8<-- [end:series]
    }
}
