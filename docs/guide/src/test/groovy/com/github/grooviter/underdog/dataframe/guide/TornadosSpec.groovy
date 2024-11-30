package com.github.grooviter.underdog.dataframe.guide


import com.github.grooviter.underdog.Underdog
import spock.lang.Specification

import java.time.LocalDate

class TornadosSpec extends Specification {
    def "metadata"() {
        setup:
        // tag::read_csv[]
        def tornadoes = Underdog.df().read_csv("src/test/resources/data/tornadoes_1950-2014.csv")
        // end::read_csv[]

        // tag::shape[]
        def (int rows, int cols) = tornadoes.shape()
        // end::shape[]

        // tag::shape2[]
        println(tornadoes.shape())
        // end::shape2[]

        // tag::first[]
        tornadoes.head(3)
        // end::first[]

        expect:
        // tag::columns[]
        tornadoes.columns
        // end::columns[]
        rows == 59945
        cols == 11

        and:
        // tag::describe[]
        tornadoes.describe()
        // end::describe[]
        tornadoes.head(3).size() == 3
    }

    def "schema"() {
        setup:
        def tornadoes = Underdog.df().read_csv("src/test/resources/data/tornadoes_1950-2014.csv")

        // tag::schema[]
        // getting tornadoes schema
        def schema = tornadoes.schema()
        // end::schema[]

        // tag::schema_tune[]
        def customSchema = schema[schema['Column Type'] == 'DOUBLE']
        // end::schema_tune[]

        // getting col names
        def colsOfInterest = customSchema.loc['Column Name'] as String[]

        // narrowing down tornadoes dataframe
        def smallerDataFrame = tornadoes[colsOfInterest]

        smallerDataFrame.head(3)

        expect:
        smallerDataFrame.columns.size() == 3
    }

    def "mapping"() {
        setup:
        def tornadoes = Underdog.df().read_csv("src/test/resources/data/tornadoes_1950-2014.csv")

        // tag::mapping[]
        def monthSeries = tornadoes["Date"](LocalDate, String) {
            it.format("MMMM")
        }
        // end::mapping[]

        // tag::add_series[]
        tornadoes['month'] = monthSeries
        // end::add_series[]

        // tag::remove_series[]
        tornadoes = tornadoes - tornadoes['Date']
        // end::remove_series[]

        expect:
        "Date" !in tornadoes.columns

        and:
        "month" in tornadoes.columns
    }

    def "sorting"() {
        when:
        def tornadoes = Underdog.df().read_csv("src/test/resources/data/tornadoes_1950-2014.csv")

        // tag::sorting_1[]
        def df1 = tornadoes.sort_values(by: 'Fatalities')
        // end::sorting_1[]

        // tag::sorting_2[]
        def df2 = tornadoes.sort_values(by: '-Fatalities')
        // end::sorting_2[]

        // tag::sorting_3[]
        def df3 = tornadoes.sort_values(by: ['Fatalities', 'State'])
        // end::sorting_3[]

        def (state) = tornadoes
            .sort_values(by: [sort_field, 'State'])
            .first().loc["State"] as String[]

        then:
        df1
        df2
        df3
        state == expected_state

        where:
        sort_field    | expected_state
        "Fatalities"  | "AL"
        "-Fatalities" | "MO"
    }

    def "column describe"() {
        when:
        def tornadoes = Underdog.df().read_csv("src/test/resources/data/tornadoes_1950-2014.csv")
        // tag::column_describe[]
        def columnStats = tornadoes["Fatalities"].describe()

        println(columnStats)
        // end::column_describe[]
        then:
        columnStats
    }

    def "filtering"() {
        when:
        // tag::filtering[]

        // reading tornadoes
        def ts = Underdog.df().read_csv("src/test/resources/data/tornadoes_1950-2014.csv")

        // adding a new series to dataframe with the name of the month
        ts['month'] = ts["Date"](LocalDate, String) { it.format("MMMM") }

        // filtering
        def result = ts[
            ts['Fatalities'] > 0 &                   // at least 1 fatalities
            ts['month'] == "April" &                 // in the month of April
            (ts['Width'] > 300 | ts['Length'] > 10)  // a tornado with a
        ]

        // selecting only two columns
        def stateAndDate = result['State', 'Date']
        // end::filtering[]
        then:
        stateAndDate.columns.size() == 2
    }

    def "summarizing"() {
        when:
        // tag::summarizing[]
        def tornadoes = Underdog.df().read_csv("src/test/resources/data/tornadoes_1950-2014.csv")

        def injuriesByScale = tornadoes
            .agg(Injuries: "median")
            .by("Scale")
            .sort_values(by: "Scale")

        injuriesByScale.name = "Median Injuries by Tornado Scale"
        // end::summarizing[]

        then:
        injuriesByScale.name == "Median Injuries by Tornado Scale"
    }

    def "crosstabs"() {
        when:
        def tornadoes = Underdog.df().read_csv("src/test/resources/data/tornadoes_1950-2014.csv")
        // tag::crosstabs[]
        def crossTab = tornadoes.xTabCounts(labels: 'State', values: 'Scale')

        crossTab.head()
        // end::crosstabs[]
        then:
        crossTab
    }

    def "getting only those tornadoes that occurred in the summer."() {
        when:
        // tag::summer[]
        def ts = Underdog.df().read_csv("src/test/resources/data/tornadoes_1950-2014.csv")

        // adding some series to the dataframe to make filtering easier
        ts['month']      = ts['Date'](Date, String) { it.format("MMMM") }
        ts['dayOfMonth'] = ts['Date'](Date, Integer) { it.format("dd").toInteger() }

        // filtering
        def summer = ts[
            (ts['month'] == 'June' & ts['dayOfMonth'] > 21) |    // after June the 21st or...
            (ts['month'] in ['July', 'August']) |                // in July or August or...
            (ts['month'] == 'September' & ts['dayOfMonth'] < 22) // before September the 22nd
        ]
        // end::summer[]

        // tag::summer_lag[]
        // sorting by Date and Time series
        summer = summer.sort_values(by: ['Date', 'Time'])

        // creating a series with lagged dates
        summer['Lagged'] = summer['Date'].lag(1)

        // creating a series with delta days between lagged dates and summer dates
        summer['Delta'] = summer['Lagged'] - summer['Date']
        // end::summer_lag[]

        // tag::summary[]
        // creating year series to be able to group by it
        summer['year'] = summer['Date'](Date, String) { it.format("YYYY") }

        // aggregating delta
        def summary = summer.agg(Delta: ["mean", "count"]).by("year")

        // print out summary
        println(summary)
        // end::summary[]

        // tag::mean_of_series[]
        def meanOfSeries = summary.iloc[__, 1].mean()
        // end::mean_of_series[]

        then: "getting the mean of the second column"
        (1.82..1.83).containsWithinBounds(meanOfSeries)
    }
}
