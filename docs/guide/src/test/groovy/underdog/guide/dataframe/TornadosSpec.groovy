package underdog.guide.dataframe


import underdog.Underdog
import spock.lang.Specification

import java.time.LocalDate

class TornadosSpec extends Specification {
    def "metadata"() {
        setup:
        // --8<-- [start:read_csv]
        def tornadoes = Underdog.df().read_csv("src/test/resources/data/tornadoes_1950-2014.csv")
        // --8<-- [end:read_csv]

        // --8<-- [start:shape]
        def (int rows, int cols) = tornadoes.shape()
        // --8<-- [end:shape]

        // --8<-- [start:shape2]
        println(tornadoes.shape())
        // --8<-- [end:shape2]

        // --8<-- [start:first]
        tornadoes.head(3)
        // --8<-- [end:first]

        expect:
        // --8<-- [start:columns]n
        tornadoes.columns
        // --8<-- [end:columns]
        rows == 59945
        cols == 11

        and:
        // --8<-- [start:describe]
        tornadoes.describe()
        // --8<-- [end:describe]
        tornadoes.head(3).size() == 3
    }

    def "schema"() {
        setup:
        def tornadoes = Underdog.df().read_csv("src/test/resources/data/tornadoes_1950-2014.csv")

        // --8<-- [start:schema]
        // getting tornadoes schema
        def schema = tornadoes.schema()
        // --8<-- [end:schema]

        // --8<-- [start:schema_tune]
        def customSchema = schema[schema['Column Type'] == 'DOUBLE']
        // --8<-- [end:schema_tune]

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

        // --8<-- [start:mapping]
        def monthSeries = tornadoes["Date"](LocalDate, String) {
            it.format("MMMM")
        }
        // --8<-- [end:mapping]

        // --8<-- [start:add_series]
        tornadoes['month'] = monthSeries
        // --8<-- [end:add_series]

        // --8<-- [start:remove_series]
        tornadoes = tornadoes - tornadoes['Date']
        // --8<-- [end:remove_series]

        expect:
        "Date" !in tornadoes.columns

        and:
        "month" in tornadoes.columns
    }

    def "sorting"() {
        when:
        def tornadoes = Underdog.df().read_csv("src/test/resources/data/tornadoes_1950-2014.csv")

        // --8<-- [start:sorting_1]
        def df1 = tornadoes.sort_values(by: 'Fatalities')
        // --8<-- [end:sorting_1]

        // --8<-- [start:sorting_2]
        def df2 = tornadoes.sort_values(by: '-Fatalities')
        // --8<-- [end:sorting_2]

        // --8<-- [start:sorting_3]
        def df3 = tornadoes.sort_values(by: ['Fatalities', 'State'])
        // --8<-- [end:sorting_3]

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
        // --8<-- [start:column_describe]
        def columnStats = tornadoes["Fatalities"].describe()

        println(columnStats)
        // --8<-- [end:column_describe]
        then:
        columnStats
    }

    def "filtering"() {
        when:
        // --8<-- [start:filtering]

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
        // --8<-- [end:filtering]
        then:
        stateAndDate.columns.size() == 2
    }

    def "summarizing"() {
        when:
        // --8<-- [start:summarizing]
        def tornadoes = Underdog.df().read_csv("src/test/resources/data/tornadoes_1950-2014.csv")

        def injuriesByScale = tornadoes
            .rename("Median Injuries by Tornado Scale")
            .agg(Injuries: "median")
            .by("Scale")
            .sort_values(by: "Scale")
        // --8<-- [end:summarizing]

        then:
        injuriesByScale.name == "Median Injuries by Tornado Scale summary"
    }

    def "crosstabs"() {
        when:
        def tornadoes = Underdog.df().read_csv("src/test/resources/data/tornadoes_1950-2014.csv")
        // --8<-- [start:crosstabs]
        def crossTab = tornadoes.xTabCounts(labels: 'State', values: 'Scale')

        crossTab.head()
        // --8<-- [end:crosstabs]
        then:
        crossTab
    }

    def "getting only those tornadoes that occurred in the summer."() {
        when:
        // --8<-- [start:summer]
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
        // --8<-- [end:summer]

        // --8<-- [start:summer_lag]
        // sorting by Date and Time series
        summer = summer.sort_values(by: ['Date', 'Time'])

        // creating a series with lagged dates
        summer['Lagged'] = summer['Date'].lag(1)

        // creating a series with delta days between lagged dates and summer dates
        summer['Delta'] = summer['Lagged'] - summer['Date']
        // --8<-- [end:summer_lag]

        // --8<-- [start:summary]
        // creating year series to be able to group by it
        summer['year'] = summer['Date'](Date, String) { it.format("YYYY") }

        // aggregating delta
        def summary = summer.agg(Delta: ["mean", "count"]).by("year")

        // print out summary
        println(summary)
        // --8<-- [end:summary]

        // --8<-- [start:mean_of_series]
        def meanOfSeries = summary.iloc[__, 1].mean()
        // --8<-- [end:mean_of_series]

        then: "getting the mean of the second column"
        (1.82..1.83).containsWithinBounds(meanOfSeries)
    }
}
