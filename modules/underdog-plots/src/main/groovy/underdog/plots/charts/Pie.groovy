package underdog.plots.charts

import groovy.transform.InheritConstructors
import groovy.transform.NamedParam
import groovy.transform.NamedVariant
import underdog.DataFrame
import underdog.Series
import underdog.plots.Options
import underdog.plots.dsl.series.PieSeries

/**
 * A pie chart (or a circle chart) is a circular statistical graphic which is divided into slices
 * to illustrate numerical proportion.
 *
 * @link https://en.wikipedia.org/wiki/Pie_chart
 * @since 0.1.0
 */
@InheritConstructors
class Pie extends Chart {

    /**
     * Creates a pie chart from a {@link DataFrame} the dataframe <b>MUST</b> have two series "names" and "values".
     * The 'names' values contain the name of the slices and the series 'values' the portion of each slice.
     *
     * @param dataframe an instance of {@link DataFrame} containing series 'names' and 'values'
     * @param chartTitle the chart title
     * @param chartSubtitle the chart subtitle
     * @return an instance of {@link Options}
     * @since 0.1.0
     */
    @NamedVariant
    Options pie(
        DataFrame dataFrame,
        @NamedParam(required = false, value='title') String chartTitle = "",
        @NamedParam(required = false, value='subtitle') String chartSubtitle = ''
    ) {
        return pie(
            dataFrame['names'],
            dataFrame['values'],
            dataFrame.hasSeries("colors") ? dataFrame['colors'] : null,
            chartTitle,
            chartSubtitle)
    }

    /**
     * Creates a pie chart from two {@link Series} instances. First instance represents slices names and the second
     * represent how big is each slice.
     *
     * @param names instance of {@link Series} containing the names of the slices
     * @param values instance of {@link Series} containing how big each slice is gonna be
     * @param chartTitle the chart title
     * @param chartSubtitle the chart subtitle
     * @return an instance of {@link Options}
     * @since 0.1.0
     */
    @NamedVariant
    Options pie(
        Series names,
        Series values,
        Series colors,
        @NamedParam(required = false, value='title') String chartTitle = "",
        @NamedParam(required = false, value='subtitle') String chartSubtitle = ''
    ) {
        Map<String, String> colorMappings = colors
            ? [names.toList(), colors.toList()].transpose().collectEntries() as Map<String, String>
            : [:]
        return pie(names.toList(), values.toList(), colorMappings, chartTitle, chartSubtitle)
    }

    /**
     * Creates a pie chart from two list. First list represents slice names and the second
     * represent how big is each slice.
     *
     * @param names list containing the names of the slices
     * @param values list containing how big each slice is gonna be
     * @param chartTitle the chart title
     * @param chartSubtitle the chart subtitle
     * @return an instance of {@link Options}
     * @since 0.1.0
     */
    @NamedVariant
    Options pie(
        List names,
        List values,
        @NamedParam(required = false) Map<String, String> colorMap = [:],
        @NamedParam(required = false, value='title') String chartTitle = "",
        @NamedParam(required = false, value='subtitle') String chartSubtitle = '') {
        List<Map<String, ?>> entries = [names, values].transpose()
            .collect { name, value ->
                [name: name, value: value]
            }
        return createGridOptions(chartTitle, chartSubtitle) + create {
            series(PieSeries) {
                radius('60%')
                data(entries)
                label {
                    show(true)
                    position('inside')
                    formatter(fn('(record) => `${record.name} (${record.value})`'))
                }
                if (colorMap) {
                    itemStyle {
                        color(fn("(item) => (${toJson(colorMap)}[item.name])"))
                    }
                }
            }
        }
    }
}
