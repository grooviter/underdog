package underdog.plots.charts

import com.github.grooviter.underdog.Series
import com.github.grooviter.underdog.plots.Options
import groovy.transform.NamedParam
import groovy.transform.NamedVariant

/**
 * A scatter plot, also called a scatterplot, scatter graph, scatter chart, scattergram, or scatter diagram, is a type
 * of plot or mathematical diagram using Cartesian coordinates to display values for typically two variables for a
 * set of data
 *
 * @link https://en.wikipedia.org/wiki/Scatter_plot
 * @since 0.1.0
 */
class Scatter extends Chart {

    /**
     * Renders a scatter chart
     *
     * @param xs the x coordinate data as {@link Series} of numbers
     * @param ys a {@link Series} of numbers
     * @param group an optional data {@link Series} for highlighting certain points
     * @param xLabel label for the X axis
     * @param yLabel label for the Y axis
     * @param chartTitle title of the chart
     * @param chartSubtitle subtitle of the chart
     * @since 0.1.0
     */
    @NamedVariant
    Options scatter(
        Series xs,
        Series ys,
        @NamedParam(required = false) Series group = null,
        @NamedParam(required = false, value='title') String chartTitle = '',
        @NamedParam(required = false, value='subtitle') String chartSubtitle = '') {
        return scatter(
            xs as List<Number>,
            ys as List<Number>,
            group as List<Number>,
            xs.name,
            ys.name,
            chartTitle,
            chartSubtitle)
    }

    /**
     * Renders a scatter chart
     *
     * @param xs the x coordinate data as {@link List} of numbers
     * @param ys a {@link List} of numbers
     * @param group an optional data {@link List} for highlighting certain points
     * @param xLabel label for the X axis
     * @param yLabel label for the Y axis
     * @param chartTitle title of the chart
     * @param chartSubtitle subtitle of the chart
     * @return an instance of {@link Options}
     * @since 0.1.0
     */
    @NamedVariant
    Options scatter(
        List<Number> xs,
        List<Number> ys,
        @NamedParam(required = false) List<Number> group = [],
        @NamedParam(required = false) String xLabel = 'X',
        @NamedParam(required = false) String yLabel = 'Y',
        @NamedParam(required = false, value='title') String chartTitle = '',
        @NamedParam(required = false, value='subtitle') String chartSubtitle = '') {
        Options baseOptions =
                createGridOptions(chartTitle, chartSubtitle) +
                createXAxisOptions(xLabel) +
                createYAxisOptions(yLabel)

        if (group) {
            return baseOptions + groupOptions(xs, ys, group)
        }

        return baseOptions + Options.create {
            series {
                type "scatter"
                data([xs, ys].transpose().sort { it.find() })
            }
        }
    }

    private static Options groupOptions(List<Number> xs, List<Number> ys, List<Number> group) {
        List<Number> uniqueGroups = group.unique(false)
        return Options.create {
            legend {
                data(uniqueGroups)
                top("8%")
            }
            uniqueGroups.eachWithIndex {value, index ->
                def sortedData = [xs, ys, group].transpose().findAll { x, y, z -> z == value }.sort { it[0] }
                series {
                    type "scatter"
                    name("$index")
                    data(sortedData)
                    itemStyle {
                        borderColor('#333')
                        borderWidth(1)
                    }
                }
            }
        }
    }
}
