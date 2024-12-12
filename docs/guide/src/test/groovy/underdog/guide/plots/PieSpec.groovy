package underdog.guide.plots

import spock.lang.Specification
import underdog.Underdog

class PieSpec extends Specification {
    def "pie: simple"() {
        expect:
        // tag::simple[]
        Underdog
            .plots()
            .pie(
                ('A'..'D'), // <1>
                [9,5,6,4]   // <2>
            ).show()
        // end::simple[]
    }

    def "pie: color mapping"() {
        expect:
        // tag::color_mapping[]
        def COLORS = [ // <1>
            "Red Bull": "#101864",
            "Ferrari": "#b03641",
            "Mclaren": "#d26f30",
            "Mercedes": "#505c62"
        ]

        Underdog.plots()
            .pie(
                ["Red Bull", "Ferrari", "Mclaren", "Mercedes"], // <2>
                [9,5,6,4], // <3>
                colorMap: COLORS, // <4>
                title: "Top 4 Teams F1(tm) 2024 season",
                subtitle: "Total number of driver victories per team"
            ).show()
        // end::color_mapping[]
    }

    def "pie: dataframe"() {
        expect:
        // tag::dataframe[]
        // source map
        def df = [
            names: ('A'..'D'),
            values: (10..40).by(10),
            colors: ['red', 'pink', 'yellow', 'lightblue']
        ].toDataFrame("dataframe")

        // passing dataframe to pie plot and show it
        Underdog.plots().pie(df).show()
        // end::dataframe[]
    }

    def "pie: series"() {
        expect:
        // tag::series[]
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
                df['A'].rename("names"),  // <1>
                df['B'].rename("values"), // <2>
                df['C'].rename("colors")  // <3>
            )
            .show()
        // end::series[]
    }
}
