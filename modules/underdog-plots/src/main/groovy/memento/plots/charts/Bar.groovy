package memento.plots.charts

import com.github.grooviter.underdog.plots.Options
import groovy.transform.NamedParam
import groovy.transform.NamedVariant

class Bar extends Chart {
    /**
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
                createXAxisOptions(xLabel) +
                createYAxisOptions(yLabel) +
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
                        data([xs, ys].transpose())
                        label {
                            show(showLabels)
                            position("inside")
                        }
                    }
                }
    }

}
