package underdog.plots.charts

import groovy.transform.InheritConstructors
import groovy.transform.NamedParam
import groovy.transform.NamedVariant
import underdog.plots.Options
import underdog.plots.dsl.series.RadarSeries

/**
 * A radar chart is a graphical method of displaying multivariate data in the form of a two-dimensional chart of
 * three or more quantitative variables represented on axes starting from the same point
 *
 * @link https://en.wikipedia.org/wiki/Radar_chart
 * @since 0.1.0
 */
@InheritConstructors
class Radar extends Chart {

    /**
     * Creates a radar chart
     *
     * @param names a list with the names of each category
     * @param maxValues maximum value for each category
     * @param values values for each category
     * @param chartTitle title of the chart
     * @param chartSubtitle subtitle of the chart
     * @return an instance of {@link Options}
     * @since 0.1.0
     */
    @NamedVariant
    Options radar(
        List names,
        List maxValues,
        List values,
        @NamedParam(required = false, value='title') String chartTitle = "",
        @NamedParam(required = false, value='subtitle') String chartSubtitle = ''
    ) {
        def namesAndMax = [names, maxValues]
            .transpose()
            .collect { nameVal, maxVal ->
                [name: nameVal, max: maxVal]
            }

        def seriesNameAndValues = [
            [name: "Radar Values", value: values]
        ]

        return createGridOptions(chartTitle, chartSubtitle) +
            create {
                radar {
                    radius("50%")
                    indicator(namesAndMax)
                }
                series(RadarSeries) {
                    data(seriesNameAndValues)
                }
            }
    }
}
