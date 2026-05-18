package underdog.guide.spectacle.components

import underdog.Underdog
import underdog.graphs.Graphs
import underdog.spectacle.dsl.HtmlApplication
import underdog.spectacle.dsl.HtmlPage
import java.util.function.Function

import static underdog.guide.spectacle.components.Constants.CSS_3_COLS_RESPONSIVE

class ChartPageProvider implements Function<HtmlApplication, HtmlPage> {


    @Override
    HtmlPage apply(HtmlApplication app) {
        return HtmlPage.create(
            path: "/components/charts",
            title: "Charts",
                preTitle: "Examples",
            application: app,
            icon: 'bi bi-graph-up'
        ){
            row {
                col {
                    markdown """\
                    | In this section you can see a sample of Underdog plots rendered using `chart()`
                    | component 
                    """
                }
            }
            row {
                col(CSS_3_COLS_RESPONSIVE) {
                    // --8<-- [start:chart]
                    chart(
                        name: app.field.chart,                  // string
                        label: 'Line',                   // string
                        info: "displays series of data points connected by a straight line",  // string
                        defaultValue: null,                     // underdog.plots.Options
                    ) {
                        // return underdog.plots.Options
                        return Underdog.plots().line(1..10, 40..50)
                    }
                    // --8<-- [end:chart]
                }
                col(CSS_3_COLS_RESPONSIVE) {
                    chart(
                        name: app.field.scatterPlot,
                        label: 'Scatter Plot',
                        info: "Uses coordinates to display values for two variables for a set of data",
                        defaultValue: null,
                    ) {
                        // return underdog.plots.Options
                        return Underdog.plots().scatter(1..10, [20, 25, 30, 23, 28, 80, 73, 43, 49, 34])
                    }
                }
                col(CSS_3_COLS_RESPONSIVE) {
                    chart(
                        name: app.field.histogram,
                        label: "Histogram",
                        info: "a visual representation of the distribution of quantitative data",
                        defaultValue: null
                    ) {
                        return Underdog.plots()
                            .bar(
                                1..12,
                                [10, 12, 18, 3, 0, 20, 10, 12, 18, 3, 0, 10],
                                xLabel: "Months",
                                yLabel: "Indicator"
                            )
                    }
                }
            }
            row {
                col(CSS_3_COLS_RESPONSIVE) {
                    chart(
                        name: app.field.graphs,
                        label: "Graph",
                        info: "different types of graphs and vertices and edges using a Groovy DSL",
                        defaultValue: null
                    ) {
                        Underdog.plots().graph(Graphs.graph(String) {
                            edge('Robert', 'Thelma', relation: 'friend')
                            edge('Robert', 'Troy', relation: 'friend')
                        })
                    }
                }
                col(CSS_3_COLS_RESPONSIVE) {
                    chart(
                        name: app.field.radar,
                        label: "Radar",
                        info: "Displays multivariate data in the form of a 2-dimensional chart",
                        defaultValue: null
                    ) {
                        Underdog
                            .plots()
                            .radar(
                                    ["power", "consumption", "price"], // Name of the categories
                                    [200, 10, 100000],                 // Maximum values for each category
                                    [150, 5, 54_350]                   // Actual value for each category
                            )
                    }
                }
            }
        }
    }
}
