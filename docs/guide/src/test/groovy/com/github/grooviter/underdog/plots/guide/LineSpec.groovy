package com.github.grooviter.underdog.plots.guide

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

    def "n-lines sharing xs"() {
        expect:
        // tag::several_lines_sharing_xs[]
        Map<String, List<Number>> ys = [
            A: [1, 3, 5, 7, 10, 6, 7, 2, 7, 10],
            B: 5..15,
            C: 3..13
        ]

        Plots.plots()
            .lines(
                2000..2010,
                ys,
                title: "Progress of Teams A, B, C",
                subtitle: "Between years 2000 - 2010",
                xLabel: "Years",
                yLabel: "Wins"
            ).show()
        // end::several_lines_sharing_xs[]
    }

    def "line with series"() {
        when:
        def baseballPath = this.class
            .getResource("/data/baseball.csv")
            .file

        and:
        // tag::line_from_series[]

        // load data
        def df = Underdog.read_csv(baseballPath)

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
        def df = Underdog.read_csv(baseballPath)

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

        def teamWins = [
            BOS: df[df['Team'] == 'BOS']['W'] as List<Number>,
            ATL: df[df['Team'] == 'ATL']['W'] as List<Number>,
            CIN: df[df['Team'] == 'CIN']['W'] as List<Number>
        ]

        // tag::customize[]
        Plots.plots()
            .lines(
                df['year'].unique() as List<Number>,
                teamWins,
                title: "Team comparison (BOS, ATL, CIN)",
                subtitle: "Years 2000-2004",
                xLabel: "Years",
                yLabel: "Wins"
            ).customize {
                legend {
                    top("10%")
                    right('15%')
                    show(true)
                }
            }.show()
        // end::customize[]
        then:
        df
    }
}
