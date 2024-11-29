package com.github.grooviter.underdog.plots.guide

import com.github.grooviter.underdog.DataFrame
import com.github.grooviter.underdog.Underdog
import memento.plots.Plots
import spock.lang.Specification

class LineSpec extends Specification {

    def "simple line"() {
        expect:
        // tag::simple[]
        Plots.plots()
            .line(
                2000..2010, // <1>
                [10, 15, 18, 3, 5, 9, 10, 11, 12, 10], // <2>
                title: "Wins of Team A", // <3>
                subtitle: "Between years 2000 - 2010",
                xLabel: "Years",
                yLabel: "Wins"
            ).show()
        // end::simple[]
    }

    def "n-lines"() {
        expect:
        // tag::n_lines[]
        def x = 2000..2010
        Map<String, List<Number>> data = [
            A: [[2000, 13],[2001, 5], [2002, 7], [2003, 10], [2004,6]], // <1>
            B: [[2000, 5], [2001, 6], [2002, 7], [2003, 8], [2004, 9]],
            C: [2000..2004, 3..7].transpose() // <2>
        ]

        Plots.plots()
            .lines(
                data,
                title: "Progress of Teams A, B, C",
                subtitle: "Between years 2000 - 2010",
                xLabel: "Years",
                yLabel: "Wins"
            ).show()
        // end::n_lines[]
    }

    def "line with series"() {
        when:
        def baseballPath = this.class
            .getResource("/data/baseball.csv")
            .file

        and:
        // tag::line_from_series[]

        // load data
        def df = Underdog.df().read_csv(baseballPath)

        // filter & aggregate & sort
        df = df[df['Team'] == 'BOS']
            .agg(W: 'sum')
            .by('year')
            .sort_values(by: 'year')

        // show
        Plots.plots()
            .line(
                df['year'], // <1>
                df['Sum [W]'].rename('Wins X Years'), // <2>
                title: "Wins of 'BOS' team over time").show()
        // end::line_from_series[]
        then:
        df
    }

    def "customize"() {
        when:
        def baseballPath = this.class
                .getResource("/data/baseball.csv")
                .file

        and:
        // loading
        def df = Underdog.df().read_csv(baseballPath)

        // filtering
        df = df[
            df['Team'] in ['BOS', 'ATL', 'CIN'] &
            df['year'] in 2000..2004
        ]

        // aggregating and sorting
        df = df.agg(W: 'sum')
            .by('year', 'Team')
            .sort_values(by: 'year')
            .rename(mapper: [('Sum [W]'): 'W'])

        def dataFrame = Underdog.df().from(
                X: df['year'].unique().sort(),
                BOS: df[df['Team'] == 'BOS']['W'],
                ATL: df[df['Team'] == 'ATL']['W'],
                CIN: df[df['Team'] == 'CIN']['W'],
        "dataset")

        // tag::customize[]
        Plots.plots()
            .lines(
                dataFrame,
                title: "Team comparison (BOS, ATL, CIN)",
                subtitle: "Years 2000-2004",
                xLabel: "Years",
                yLabel: "Wins"
            ).customize {
                legend { // <1>
                    top("10%")
                    right('15%')
                    show(true)
                }
                tooltip { // <2>
                    trigger('axis')
                }
            }.show()
        // end::customize[]
        then:
        df
    }
}
