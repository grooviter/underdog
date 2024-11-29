package memento.plots.charts

import com.github.grooviter.underdog.DataFrame
import com.github.grooviter.underdog.Series
import com.github.grooviter.underdog.plots.Options
import groovy.transform.NamedParam
import groovy.transform.NamedVariant

/**
 * A line chart or line graph, also known as curve chart, is a type of chart that displays information as a series
 * of data points called 'markers' connected by straight line segments.
 *
 * @link https://en.wikipedia.org/wiki/Line_chart
 * @since 0.1.0
 */
class Line extends Chart {

    @NamedVariant
    Options lines(
        DataFrame dataFrame,
        @NamedParam(required = false) String xLabel = 'X',
        @NamedParam(required = false) String yLabel = 'Y',
        @NamedParam(required = false, value='title') String chartTitle = "",
        @NamedParam(required = false, value='subtitle') String chartSubtitle = '',
        @NamedParam(required = false, value='smooth') boolean chartSmooth = false
    ){
        String xsName = dataFrame.columns.find { it.toLowerCase() == 'x' }
        List xs = dataFrame[xsName] as List
        return createGridOptions(chartTitle, chartSubtitle) +
                createXAxisOptions(xLabel, xs) +
                createYAxisOptions(yLabel) +
                Options.create {
                    dataFrame.columns
                        .findAll { it != xsName }
                        .each { next ->
                            series {
                                type("line")
                                name(next)
                                data(dataFrame[next] as List)
                                smooth(chartSmooth)
                            }
                        }
                }
    }

    /**
     * Renders several lines. It receives a Map. Each entry key is the name of the series and the entry
     * value is a list with the series data. For example:
     *
     * <code>
     * def series = [
     *    series_1: [[10, 20], [20, 10]],
     *    series_2: [[10, 30], [20, 70]]
     * ]
     * </code>
     *
     * Each series has a list of type [[x, y], [x, y],...]
     *
     * All lines share the same x coordinates. Every line is represented in an entry of the xs Map. The
     * entry key is the name of the series and the value is the series dataset.
     *
     * @param series data series as {@link List} of {@link List} of numbers
     * @param xLabel label for the X axis
     * @param yLabel label for the Y axis
     * @param chartTitle title of the chart
     * @param chartSubtitle subtitle of the chart
     * @param smooth whether to smooth lines or not
     * @return an instance of {@link Options}
     * @since 0.1.0
     */
    @NamedVariant
    Options lines(
        Map<String, List<List<Number>>> seriesData,
        @NamedParam(required = false) String xLabel = 'X',
        @NamedParam(required = false) String yLabel = 'Y',
        @NamedParam(required = false, value='title') String chartTitle = "",
        @NamedParam(required = false, value='subtitle') String chartSubtitle = '',
        @NamedParam(required = false, value='smooth') boolean chartSmooth = false
    ){
        return createGridOptions(chartTitle, chartSubtitle) +
        createXAxisOptions(xLabel) +
        createYAxisOptions(yLabel) +
        Options.create {
            seriesData.each { next ->
                series {
                    type("line")
                    name(next.key)
                    data(next.value)
                    smooth(chartSmooth)
                }
            }
        }
    }

    /**
     * Renders a line chart
     *
     * @param xs a {@link Series} of numbers
     * @param ys a {@link Series} of numbers
     * @param xLabel label for the X axis
     * @param yLabel label for the Y axis
     * @param chartTitle title of the chart
     * @param chartSubtitle subtitle of the chart
     * @param smooth whether to smooth lines or not
     * @return an instance of {@link Options}
     * @since 0.1.0
     */
    @NamedVariant
    Options line(
        Series xs,
        Series ys,
        @NamedParam(required = false, value='title') String chartTitle = "",
        @NamedParam(required = false, value='subtitle') String chartSubtitle = '',
        @NamedParam(required = false, value='smooth') boolean chartSmooth = false) {
        return line(xs as List<Number>, ys as List<Number>, xs.name, ys.name, chartTitle, chartSubtitle, chartSmooth)
    }

    /**
     * Renders a line chart
     *
     * @param xs a {@link List} of numbers
     * @param ys a {@link List} of numbers
     * @param xLabel label for the X axis
     * @param yLabel label for the Y axis
     * @param chartTitle title of the chart
     * @param chartSubtitle subtitle of the chart
     * @param smooth whether to smooth lines or not
     * @return an instance of {@link Options}
     * @since 0.1.0
     */
    @NamedVariant
    Options line(
        List<Number> xs,
        List<Number> ys,
        @NamedParam(required = false) String xLabel = 'X',
        @NamedParam(required = false) String yLabel = 'Y',
        @NamedParam(required = false, value='title') String chartTitle = "",
        @NamedParam(required = false, value='subtitle') String chartSubtitle = '',
        @NamedParam(required = false, value='smooth') boolean chartSmooth = false) {
        return createGridOptions(chartTitle, chartSubtitle) +
            createXAxisOptions(xLabel, xs) +
            createYAxisOptions(yLabel) +
            Options.create {
                series {
                    type("line")
                    smooth(chartSmooth)
                    data(ys)
                }
            }
    }
}
