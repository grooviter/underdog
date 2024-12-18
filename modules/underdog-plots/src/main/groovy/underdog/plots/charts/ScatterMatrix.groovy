package underdog.plots.charts

import groovy.transform.InheritConstructors
import groovy.transform.NamedParam
import groovy.transform.NamedVariant
import underdog.DataFrame
import underdog.plots.Options
import underdog.plots.dsl.series.BarSeries
import underdog.plots.dsl.series.ScatterSeries

/**
 * Draws a matrix of scatter plots
 *
 * @link https://en.wikipedia.org/wiki/Scatter_matrix
 * @since 0.1.0
 */
@InheritConstructors
class ScatterMatrix extends Chart {
    private static final int FULL_SPACE = 100
    private static final int BASE_LEFT = 15
    private static final int BASE_TOP = 15
    private static final String COLOR = "#4992ff"

    /**
     * Draws a matrix of scatter plots
     *
     * @param dataframe each dataframe series will become a feature using the series name as label
     * @param histogramBins number of bins for the distribution charts
     * @return an instance of {@link Options}
     * @since 0.1.0
     */
    @NamedVariant
    Options scatterMatrix(
        DataFrame dataFrame,
        @NamedParam(required = false)
        Integer histogramBins = 10) {
        return scatterMatrix(dataFrame.toList(), labels: dataFrame.columns, histogramBins:  histogramBins)
    }

    /**
     * Draws a matrix of scatter plots
     *
     * @param dataframe an array of arrays. Each dataframe array entry will become a feature
     * @param labels a list with the names of the features
     * @param histogramBins number of bins for the distribution charts
     * @return an instance of {@link Options}
     * @since 0.1.0
     */
    @NamedVariant
    Options scatterMatrix(
            double[][] dataframe,
            @NamedParam(required = false) List<String> labels = [],
            @NamedParam(required = false) Integer histogramBins = 10) {
        return scatterMatrix(dataframe as List<List<Number>>, labels, histogramBins)
    }
    /**
     * Draws a matrix of scatter plots
     *
     * @param dataframe a list of list. each list will become a feature
     * @param labels a list with the names of the features
     * @param histogramBins number of bins for the distribution charts
     * @return an instance of {@link Options}
     * @since 0.1.0
     */
    @NamedVariant
    Options scatterMatrix(
        List<List<Number>> dataframe,
        @NamedParam(required = false) List<String> labels = [],
        @NamedParam(required = false) Integer histogramBins = 10) {
        def xs = groupNumbersByColumn(dataframe)
        def ys = groupNumbersByColumn(dataframe)
        int gridIndexNumber = 0
        def categoryDimCount = xs.size()
        def gridWidth = (FULL_SPACE - BASE_LEFT) / categoryDimCount
        def gridHeight = (FULL_SPACE - BASE_TOP) / categoryDimCount

        return create {
            xs.eachWithIndex { x, i ->
                ys.eachWithIndex { y, j ->
                    xAxis {
                        if (labels && (j == ys.size() - 1)) {
                            name("${labels[i]}")
                            show(true)
                        } else {
                            name("x-${gridIndexNumber}")
                            show(false)
                        }
                        nameLocation("center")
                        gridIndex(gridIndexNumber)
                        splitLine { show(false) }
                        axisLabel { show(false) }
                        axisTick { show(false) }
                    }

                    yAxis {
                        if (labels && i == 0) {
                            name("${labels[j]}")
                            show(true)
                        } else {
                            name("y-${gridIndexNumber}")
                            show(false)
                        }
                        nameLocation("center")
                        gridIndex(gridIndexNumber)
                        splitLine { show(false) }
                        axisLabel { show(false) }
                        axisTick { show(false) }
                    }

                    grid {
                        show(true)
                        top("${BASE_TOP + (j * gridHeight) - (BASE_TOP / 2)}%")
                        left("${BASE_LEFT + (i * gridWidth) - (BASE_LEFT / 2)}%")
                        width("${gridWidth}%")
                        height("${gridHeight}%")
                    }

                    if (j == i) {
                        series(BarSeries) {
                            data(Histogram.createHistogramDataFrom(x.collect(), histogramBins))
                            xAxisIndex(gridIndexNumber)
                            yAxisIndex(gridIndexNumber)
                            itemStyle {
                                color(COLOR)
                            }
                        }
                    } else {
                        series(ScatterSeries) {
                            data([x, y].transpose())
                            xAxisIndex(gridIndexNumber)
                            yAxisIndex(gridIndexNumber)
                            symbolSize(3)
                            itemStyle {
                                color(COLOR)
                            }
                        }
                    }

                    gridIndexNumber++
                }
            }
        }
    }

    private static List<List<Number>> groupNumbersByColumn(List<List<Number>> dataframe) {
        return dataframe.inject([]) { agg, next ->
            next.eachWithIndex { n, i ->
                if (!agg[i]) {
                    agg[i] = []
                }
                agg[i] << next[i]
            }
            agg
        }
    }
}
