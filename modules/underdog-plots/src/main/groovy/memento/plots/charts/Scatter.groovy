package memento.plots.charts

import com.github.grooviter.underdog.Series
import com.github.grooviter.underdog.plots.Options
import groovy.transform.NamedParam
import groovy.transform.NamedVariant

/**
 *
 * @since 0.1.0
 */
class Scatter extends Chart {

    /**
     * @param xs
     * @param ys
     * @param group
     * @param xLabel
     * @param yLabel
     * @param chartTitle
     * @return
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

    /**
     * @param xs
     * @param ys
     * @param group
     * @param chartTitle
     * @return
     * @since 0.1.0
     */
    @NamedVariant
    Options scatter(
        Series xs,
        Series ys,
        @NamedParam(required = false) Series group,
        @NamedParam(required = false, value='title') String chartTitle = '',
        @NamedParam(required = false, value='subtitle') String chartSubtitle = '') {
        return scatter(
            xs as List<Number>,
            ys as List<Number>,
            xLabel: xs.name,
            yLabel: ys.name,
            title: chartTitle,
            subtitle: chartSubtitle,
            group: group as List<Number>)
    }
}
