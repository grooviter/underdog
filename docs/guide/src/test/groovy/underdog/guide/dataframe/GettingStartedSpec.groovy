package underdog.guide.dataframe

// --8<-- [start:getting_started_imports]
// importing Underdog
import underdog.Underdog
// --8<-- [end:getting_started_imports]

import spock.lang.Specification

class GettingStartedSpec extends Specification {
    def "initial example"() {
        when:
        // --8<-- [start:getting_started_simple]
        // reading data from CSV file
        def df = Underdog.df().read_csv("src/test/resources/data/tornadoes_1950-2014.csv")

        // creating new series
        df["year"] = df["date"](Date, Integer) {
            it.format("yyyy").toInteger()
        }

        df["month"] = df["date"](Date, Integer) {
            it.format("MM").toInteger()
        }

        // how many tornadoes hit Texas in 2012
        def tornadoesInTxIn2012 = df[df["State"] == "TX" & df['year'] == 2012]

        // only interested in month and scale series
        def monthsAndScale = tornadoesInTxIn2012.loc[__, ["month", "Scale"]]

        // max scale by month
        def maxScaleByYear = monthsAndScale.agg(Scale: "max").by('month')
        // --8<-- [end:getting_started_simple]

        // tag::show_plot[]
        def plot = Underdog.plots().bar(
            maxScaleByYear['month'],
            maxScaleByYear['Max [Scale]'],
            title: 'Tornadoes in Texas in 2012',
            subtitle: 'Maximum tornadoes scale reached by month',
            showLabels: true
        )

        plot.show()
        // end::show_plot[]
        then:
        tornadoesInTxIn2012.size() == 115
    }
}
