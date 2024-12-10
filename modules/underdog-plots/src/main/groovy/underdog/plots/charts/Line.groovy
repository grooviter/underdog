package underdog.plots.charts

import groovy.transform.InheritConstructors
import underdog.DataFrame
import underdog.Series
import underdog.plots.Options
import groovy.transform.NamedParam
import groovy.transform.NamedVariant
import underdog.plots.dsl.series.LineSeries

/**
 * A line chart or line graph, also known as curve chart, is a type of chart that displays information as a series
 * of data points called 'markers' connected by straight line segments.
 *
 * @link https://en.wikipedia.org/wiki/Line_chart
 * @since 0.1.0
 */
@InheritConstructors
class Line extends Chart {

    @NamedVariant
    Line addAnnotation(
        Object x,
        Object y,
        @NamedParam(required = false) String text = '',
        @NamedParam(required = false) String color = '',
        @NamedParam(required = false) String seriesName = "+INFO"
    ) {
        def markSeries = findExtraInfoMap(seriesName)

        def markPoint = [
            [*: markAreaColor(color),value: text, xAxis: x, yAxis: y, color: color]
        ]

        if (!markSeries['markPoint']) {
            markSeries['markPoint'] = [data: markPoint]
            return this
        }

        markSeries['markPoint']['data'] += markPoint
        return this
    }

    @NamedVariant
    Line addMarkAreaInX(
        Object fromX,
        Object toX,
        @NamedParam(required = false) String title = "",
        @NamedParam(required = false) String color = "",
        @NamedParam(required = false) String seriesName = "+INFO") {
        def markSeries = findExtraInfoMap(seriesName)

        def markData = [
            [
                [*: markAreaColor(color), name: title, xAxis: fromX.toString()],
                [xAxis: toX.toString()]
            ]
        ]

        if (!markSeries['markArea']) {
            markSeries['markArea'] = [data: markData]
            return this
        }

        markSeries['markArea']['data'] += markData
        return this
    }

    @NamedVariant
    Line addMarkAreaInY(
            Object fromY,
            Object toY,
            @NamedParam(required = false) String title = "",
            @NamedParam(required = false) String color = "",
            @NamedParam(required = false) String seriesName = "+INFO") {
        def markSeries = findExtraInfoMap(seriesName)

        def markData = [
            [
                [*: markAreaColor(color), name: title, yAxis: fromY.toString()],
                [yAxis: toY.toString()]
            ]
        ]

        if (!markSeries['markArea']) {
            markSeries['markArea'] = [data: markData]
            return this
        }

        markSeries['markArea']['data'] += markData
        return this
    }

    private Map findExtraInfoMap(String seriesName) {
        if (!map.series) {
            this.map.series = [[name: seriesName, type: "line"]]
        }

        if (map.series instanceof Map){
            map.series = [map.series]
        }

        if (!map.series?.find { it['name'] == seriesName }) {
            this.map.series += [name: seriesName, type: "line"]
        }

        return map.series?.find { it['name'] == seriesName } as Map
    }

    private static Map markAreaColor(String color) {
        if (color) {
            return [itemStyle: [color: color]]
        }
        return [:]
    }

    @NamedVariant
    Line lines(
        DataFrame dataFrame,
        @NamedParam(required = false) String xLabel = 'X',
        @NamedParam(required = false) String yLabel = 'Y',
        @NamedParam(required = false, value='title') String chartTitle = "",
        @NamedParam(required = false, value='subtitle') String chartSubtitle = '',
        @NamedParam(required = false, value='smooth') boolean chartSmooth = false
    ){
        String xsName = dataFrame.columns.find { it.toLowerCase() == 'x' }

        if (!xsName) {
            throw new RuntimeException("No X series found in dataframe")
        }

        List xs = dataFrame[xsName] as List
        Options options = createGridOptions(chartTitle, chartSubtitle) +
                createXAxisOptions(xLabel, xs) +
                createYAxisOptions(yLabel) +
                create {
                    dataFrame.columns
                        .findAll { it != xsName }
                        .each { next ->
                            series(LineSeries) {
                                name(next)
                                data(dataFrame[next] as List)
                                smooth(chartSmooth)
                            }
                        }
                }
        return new Line(options)
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
    Line lines(
        Map<String, List<List<Number>>> seriesData,
        @NamedParam(required = false) String xLabel = 'X',
        @NamedParam(required = false) String yLabel = 'Y',
        @NamedParam(required = false, value='title') String chartTitle = "",
        @NamedParam(required = false, value='subtitle') String chartSubtitle = '',
        @NamedParam(required = false, value='smooth') boolean chartSmooth = false
    ){
        Options options = createGridOptions(chartTitle, chartSubtitle) +
        createXAxisOptions(xLabel) +
        createYAxisOptions(yLabel) +
        create {
            seriesData.each { next ->
                series(LineSeries) {
                    type("line")
                    name(next.key)
                    data(next.value)
                    smooth(chartSmooth)
                }
            }
        }
        return new Line(options)
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
    Line line(
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
    Line line(
        List<Number> xs,
        List<Number> ys,
        @NamedParam(required = false) String xLabel = 'X',
        @NamedParam(required = false) String yLabel = 'Y',
        @NamedParam(required = false, value='title') String chartTitle = "",
        @NamedParam(required = false, value='subtitle') String chartSubtitle = '',
        @NamedParam(required = false, value='smooth') boolean chartSmooth = false) {
        Options options = createGridOptions(chartTitle, chartSubtitle) +
            createXAxisOptions(xLabel, xs) +
            createYAxisOptions(yLabel) +
            create {
                series(LineSeries) {
                    smooth(chartSmooth)
                    data(ys)
                }
            }
        return new Line(options)
    }
}
