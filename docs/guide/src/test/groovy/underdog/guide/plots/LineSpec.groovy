package underdog.guide.plots


import underdog.Underdog
import underdog.plots.Plots
import spock.lang.Specification

class LineSpec extends Specification {

    def "simple line"() {
        expect:
        // --8<-- [start:simple]
        def line = Plots.plots()
            .line(
                // You can use a **range or a list** for X axis
                2000..2010,
                // You can use a **range or a list** for the Y axis
                [10, 15, 18, 3, 5, 9, 10, 11, 12, 10],
                // Optional attributes
                title: "Wins of Team A",
                subtitle: "Between years 2000 - 2010",
                xLabel: "Years",
                yLabel: "Wins"
            )

        line.show()
        // --8<-- [end:simple]
        Plots.show(line, theme: "dark")
    }

    def "n-lines"() {
        expect:
        // --8<-- [start:n_lines]
        Map<String, List<Number>> data = [
            // A list of lists of 2 elements [[x1, y1], [x2, y2],..., [xn, yn]]
            A: [[2000, 13],[2001, 5], [2002, 7], [2003, 10], [2004,6]],
            B: [[2000, 5], [2001, 6], [2002, 7], [2003, 8], [2004, 9]],
            // Using [listX, listY]transpose()` == [[x1, y1], [x2, y2],..., [xn, yn]]
            C: [2000..2004, 3..7].transpose()
        ]

        def plot = Underdog.plots()
            .lines(
                data,
                title: "Progress of Teams A, B, C",
                subtitle: "Between years 2000 - 2010",
                xLabel: "Years",
                yLabel: "Wins"
            )

        plot.show()
        // --8<-- [end:n_lines]
        Plots.show(plot, theme: "dark")
    }

    def "line with series"() {
        when:
        def baseballPath = this.class
            .getResource("/data/baseball.csv")
            .file

        and:
        // --8<-- [start:line_from_series]

        // load data
        def df = Underdog.df().read_csv(baseballPath)

        // filter & aggregate & sort
        df = df[df['Team'] == 'BOS']
            .agg(W: 'sum')
            .by('year')
            .sort_values(by: 'year')

        // show
        def plot = Underdog.plots()
            .line(
                // using `year` series for X axis
                df['year'],
                // renaming series to `Wins X Years` and using it for Y axis
                df['Sum [W]'].rename('Wins X Years'),
                title: "Wins of 'BOS' team over time")

        plot.show()
        // --8<-- [end:line_from_series]
        Plots.show(plot, theme: "dark")
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
            .renameSeries(mapper: [('Sum [W]'): 'W'])

        def dataFrame = Underdog.df().from(
                X: df['year'].unique().sort(),
                BOS: df[df['Team'] == 'BOS']['W'],
                ATL: df[df['Team'] == 'ATL']['W'],
                CIN: df[df['Team'] == 'CIN']['W'],
        "dataset")

        // --8<-- [start:customize]
        def plot = Underdog.plots()
            .lines(
                dataFrame,
                title: "Team comparison (BOS, ATL, CIN)",
                subtitle: "Years 2000-2004",
                xLabel: "Years",
                yLabel: "Wins"
            ).customize {
                // Adding legend in the top right corner
                legend {
                    top("10%")
                    right('15%')
                    show(true)
                }
                // Adding tooltip of type `axis`
                tooltip {
                    trigger('axis')
                }
            }

        plot.show()
        // --8<-- [end:customize]
        Plots.show(plot, theme: "dark")
        then:
        df
    }
}
