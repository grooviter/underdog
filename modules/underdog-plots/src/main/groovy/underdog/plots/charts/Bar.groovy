package underdog.plots.charts

import groovy.transform.InheritConstructors
import underdog.Series
import underdog.plots.Options
import groovy.transform.NamedParam
import groovy.transform.NamedVariant

/**
 * A bar chart or bar graph is a chart or graph that presents categorical data with rectangular bars with heights or
 * lengths proportional to the values that they represent. The bars can be plotted vertically or horizontally
 *
 * @link https://en.wikipedia.org/wiki/Bar_chart
 * @since 0.1.0
 */
@InheritConstructors
class Bar extends Chart {

    /**
     * Renders a bar chart
     *
     * @param xs a {@link Series} of numbers
     * @param ys a {@link Series} of numbers
     * @param chartTitle title of the chart
     * @param chartSubtitle subtitle of the chart
     * @param showLabels whether to show or not to show bars labels
     * @return an instance of {@link Options}
     * @since 0.1.0
     */
    @NamedVariant
    Options bar(
        Series xs,
        Series ys,
        @NamedParam(required = false, value='title') String chartTitle = '',
        @NamedParam(required = false, value='subtitle') String chartSubtitle = '',
        @NamedParam(required = false) boolean showLabels = false) {
        return bar(xs as List<Number>, ys as List<Number>, xs.name, ys.name, chartTitle, chartSubtitle, showLabels)
    }

    /**
     * Renders a bar chart
     *
     * @param xs a {@link List} of numbers
     * @param ys a {@link List} of numbers
     * @param xLabel label for the X axis
     * @param yLabel label for the Y axis
     * @param chartTitle title of the chart
     * @param chartSubtitle subtitle of the chart
     * @param showLabels whether to show or not to show bars labels
     * @return an instance of {@link Options}
     * @since 0.1.0
     */
    @NamedVariant
    Options bar(
        List<Number> xs,
        List<Number> ys,
        @NamedParam(required = false) String xLabel = 'X',
        @NamedParam(required = false) String yLabel = 'Y',
        @NamedParam(required = false, value='title') String chartTitle = '',
        @NamedParam(required = false, value='subtitle') String chartSubtitle = '',
        @NamedParam(required = false) boolean showLabels = false) {
        return createGridOptions(chartTitle, chartSubtitle) +
            Options.create {
                xAxis {
                    type 'value'
                    name(xLabel)
                    nameLocation("center")
                    nameTextStyle {
                        padding(30)
                    }
                }
                yAxis {
                    type 'value'
                    name(yLabel)
                    nameLocation("center")
                    nameTextStyle {
                        padding(30)
                    }
                }
                series {
                    name 'Direct'
                    type 'bar'
                    data([xs, ys].transpose())
                    label {
                        show(showLabels)
                        position("inside")
                    }
                }
            }
    }
}
