package underdog.plots.charts

import groovy.transform.InheritConstructors
import underdog.Series
import underdog.plots.Options
import groovy.transform.NamedParam
import groovy.transform.NamedVariant
import underdog.plots.dsl.series.BarSeries

/**
 * A histogram is a visual representation of the distribution of quantitative data. To construct a histogram,
 * the first step is to "bin" (or "bucket") the range of values— divide the entire range of values into a series
 * of intervals—and then count how many values fall into each interval. The bins are usually specified as consecutive,
 * non-overlapping intervals of a variable. The bins (intervals) are adjacent and are typically
 * (but not required to be) of equal size
 *
 * @link https://en.wikipedia.org/wiki/Histogram
 * @since 0.1.0
 */
@InheritConstructors
class Histogram extends Chart {

    /**
     * Renders a histogram chart
     *
     * @param xs a {@link Series} of numbers
     * @param yLabel label for the Y axis
     * @param chartTitle title of the chart
     * @param chartSubtitle subtitle of the chart
     * @param bins how many bins you want the data to fit on
     * @param showLabels whether to show or not to show bars labels
     * @return an instance of {@link Options}
     * @since 0.1.0
     */
    @NamedVariant
    Options histogram(
        Series xs,
        @NamedParam(required = false) String yLabel = 'Y',
        @NamedParam(required = false, value='title') String chartTitle = '',
        @NamedParam(required = false, value='subtitle') String chartSubtitle = '',
        @NamedParam(required = false) int bins = 20,
        @NamedParam(required = false) boolean showLabels = false) {
        return histogram(xs as List<Number>, xs.name, yLabel, chartTitle, chartSubtitle, bins, showLabels)
    }

    /**
     * Renders a histogram chart
     *
     * @param xs a {@link List} of numbers
     * @param xLabel label for the X axis
     * @param yLabel label for the Y axis
     * @param chartTitle title of the chart
     * @param chartSubtitle subtitle of the chart
     * @param bins how many bins you want the data to fit on
     * @param showLabels whether to show or not to show bars labels
     * @return an instance of {@link Options}
     * @since 0.1.0
     */
    @NamedVariant
    Options histogram(
        List<Number> xs,
        @NamedParam(required = false) String xLabel = 'X',
        @NamedParam(required = false) String yLabel = 'Y',
        @NamedParam(required = false, value='title') String chartTitle = '',
        @NamedParam(required = false, value='subtitle') String chartSubtitle = '',
        @NamedParam(required = false) int bins = 20,
        @NamedParam(required = false) boolean showLabels = false) {
        def min = Math.floor(xs.min().toDouble()).toInteger()
        def max = Math.ceil(xs.max().toDouble()).toInteger()
        def binSize = Math.ceil((max - min) / bins).toInteger()

        def x = (0..bins).inject([min]) { agg, next ->
            agg << (agg[-1] + binSize)
            agg
        } as List<Number>

        def y = x.collect { xn ->
            def currentBin = xs.sort().takeWhile { xsn -> xsn < xn }
            def currentBinFreq = currentBin.size()
            xs.removeAll(currentBin)
            return currentBinFreq
        }

        def dataset = [x, y].transpose()

        return createGridOptions(chartTitle, chartSubtitle) +
            createXAxisOptions(xLabel) +
            createYAxisOptions(yLabel) +
            create {
            xAxis {
                type 'value'
            }
            yAxis {
                type 'value'
            }
            series(BarSeries) {
                name 'Direct'
                barWidth '100%'
                data(dataset)
                label {
                    show(showLabels)
                    position("inside")
                }
            }
        }
    }
}
