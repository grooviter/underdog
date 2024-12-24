package underdog.plots.charts

import groovy.transform.InheritConstructors
import underdog.plots.Options
import underdog.plots.dsl.series.HeatmapSeries

/**
 * @since 0.1.0
 */
@InheritConstructors
class CorrelationMatrix extends Chart {

    /**
     * @param xs
     * @param ys
     * @param heatmapData
     * @return
     * @since 0.1.0
     */
    Options correlationMatrix(
        List labels,
        double[][] heatmapData
    ) {
        def finalData = []
        labels.eachWithIndex { Object left, int i ->
            labels.eachWithIndex { Object right, int j ->
                finalData << [i, j, heatmapData[i][j]]
            }
        }

        return create {
            grid {
                bottom('100')
            }
            xAxis {
                show(true)
                type('category')
                data(labels)
                axisLabel {
                    rotate(90)
                }
            }
            yAxis {
                show(true)
                inverse(true)
                data(labels)
            }
            visualMap {
                min(0)
                max(1)
                show(false)
            }
            series(HeatmapSeries) {
                data(finalData)
                label {
                    show(true)
                }
            }
        }
    }
}
