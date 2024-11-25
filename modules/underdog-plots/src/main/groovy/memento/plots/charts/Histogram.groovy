package memento.plots.charts

import com.github.grooviter.underdog.plots.Options
import groovy.transform.NamedParam
import groovy.transform.NamedVariant

/**
 * @since 0.1.0
 */
class Histogram extends Chart {

    /**
     * @since 0.1.0
     */
    @NamedVariant
    Options histogram(
        List<Number> xs,
        @NamedParam(required = false, value='title') String chartTitle = '',
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

        return createGridOptions(chartTitle) +
            createXAxisOptions('X') +
            createYAxisOptions('Y') +
            Options.create {
            xAxis {
                type 'value'
            }
            yAxis {
                type 'value'
            }
            series {
                name 'Direct'
                type 'bar'
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
