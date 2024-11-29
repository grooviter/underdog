package com.github.grooviter.underdog.dataframe.guide

// tag::getting_started_simple_imports[]
// importing Underdog
import com.github.grooviter.underdog.Underdog
import com.github.grooviter.underdog.plots.Options

// end::getting_started_simple_imports[]
import memento.plots.Plots
import spock.lang.Specification

class GettingStartedSpec extends Specification {
    def "initial example"() {
        when:
        def csvFilePath = this.class.getResource("/data/tornadoes_1950-2014.csv").file
        // tag::getting_started_simple[]
        // reading data from CSV file
        def df = Underdog.df().read_csv(csvFilePath)

        // creating new series
        df["year"] = df["date"](Date, Integer) { it.format("yyyy").toInteger() }
        df["month"] = df["date"](Date, Integer) { it.format("MM").toInteger() }

        // how many tornadoes hit Texas in 2012
        def tornadoesInTxIn2012 = df[df["State"] == "TX" & df['year'] == 2012]

        // only interested in month and scale series
        def monthsAndScale = tornadoesInTxIn2012.loc[__, ["month", "Scale"]]

        // max scale by month
        def maxScaleByYear = monthsAndScale.agg(Scale: "max").by('month')
        // end::getting_started_simple[]

        // tag::show_plot[]
        def plot = Plots.plots()
            .bar(
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
