package memento.plots.charts

import com.github.grooviter.underdog.plots.Options
import groovy.transform.NamedParam
import groovy.transform.NamedVariant

/**
 * @since 0.1.0
 */
class Line extends Chart {
    /**
     * @param xs
     * @param ys
     * @param chartTitle
     * @param smooth
     * @return
     * @since 0.1.0
     */
    @NamedVariant
    Options lines(
        List<Number> xs,
        Map<String, List<Number>> ys,
        @NamedParam(required = false) String xLabel = 'X',
        @NamedParam(required = false) String yLabel = 'Y',
        @NamedParam(required = false, value='title') String chartTitle = "",
        @NamedParam(required = false, value='smooth') boolean chartSmooth = false) {
        return createGridOptions(chartTitle) +
            createXAxisOptions(xLabel, xs) +
            createYAxisOptions(yLabel) +
            Options.create {
                ys.each { next ->
                    println next
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
     * @param xs
     * @param ys
     * @param chartTitle
     * @param smooth
     * @return
     * @since 0.1.0
     */
    @NamedVariant
    Options line(
        List<Number> xs,
        List<Number> ys,
        @NamedParam(required = false) String xLabel = 'X',
        @NamedParam(required = false) String yLabel = 'Y',
        @NamedParam(required = false, value='title') String chartTitle = "",
        @NamedParam(required = false, value='smooth') boolean chartSmooth = false) {
        return createGridOptions(chartTitle) +
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

    /**
     * @param xs
     * @param ys
     * @param options
     * @return
     * @since 0.1.0
     */
    Options line(
        List<Number> xs,
        List<Number> ys, @DelegatesTo(Options) Closure options){
        return line(xs, ys) + Options.create(options)
    }
}
