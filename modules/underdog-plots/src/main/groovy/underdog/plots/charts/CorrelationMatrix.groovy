package underdog.plots.charts

import groovy.transform.InheritConstructors
import underdog.plots.Options
import underdog.plots.dsl.series.HeatmapSeries
import underdog.plots.dsl.visualmap.PiecewiseVisualMap

/**
 * @since 0.1.0
 */
@InheritConstructors
class CorrelationMatrix extends Chart {

    private static final String NAN_COlOR = 'rgba(250, 250, 250, 0.5)'
    private static final Integer NAN_NUMBER = -999
    private static final String NAN_STRING = 'NaN'
    private static final Integer LABEL_TRUNCATE_AT_WIDTH = 10
    private static final String LABEL_TRUNCATE_AT_WIDTH_ELLIPSIS = "..."

    /**
     * @param xs
     * @param ys
     * @param heatmapData
     * @return
     * @since 0.1.0
     */
    @SuppressWarnings('GrMethodMayBeStatic')
    Options correlationMatrix(
        List labels,
        double[][] heatmapData
    ) {
        List finalData = []
        labels.eachWithIndex { Object left, int i ->
            labels.eachWithIndex { Object right, int j ->
                finalData << [i, j, handleHowToRenderNaN(heatmapData[i][j])]
            }
        }

        return create {
            grid {
                bottom('20%')
                left("20%")
            }
            xAxis {
                show(true)
                type('category')
                data(resolveLabels(labels))
                axisLabel { rotate(90) }
                splitArea { show(true) }
            }
            yAxis {
                show(true)
                inverse(true)
                data(resolveLabels(labels))
                splitArea { show(true) }
                nameTruncate { maxWidth(15) }
            }
            visualMap(PiecewiseVisualMap) {
                show(false)
                inRange {
                    color(resolveColors())
                }
                pieces {
                    min(-999)
                    max(-1)
                }
                correlationRanges.each { Double i ->
                    pieces {
                        min(i)
                        max(i + 0.5)
                    }
                }
            }
            series(HeatmapSeries) {
                data(finalData)
                label {
                    show(true)
                    formatter(fn(resolveCellFormatterFunction()))
                }
            }
        }
    }

    private static List<String> resolveLabels(List<String> labels) {
        return labels.collect {
            it.toString().with {
                it.size() > LABEL_TRUNCATE_AT_WIDTH
                    ? "${substring(0, LABEL_TRUNCATE_AT_WIDTH)}${LABEL_TRUNCATE_AT_WIDTH_ELLIPSIS}"
                    : it
            }
        }
    }

    private static List<String> resolveColors() {
        return [NAN_COlOR] + ['#fff9d7', '#ffd700', 'orange', 'red']
    }

    private static String resolveCellFormatterFunction() {
        return '(series) => series.data[2] == %s ? \'%s\' : series.data[2]'.formatted(NAN_NUMBER, NAN_STRING)
    }

    private static List<Double> getCorrelationRanges() {
        return (-1..1).by(0.5).take(4) as List<Double>
    }

    private static Object handleHowToRenderNaN(Object o) {
        return o.toString() == NAN_STRING ? NAN_NUMBER : o
    }
}
