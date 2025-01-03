package underdog.ta

import groovy.transform.NamedParam
import groovy.transform.NamedVariant
import underdog.Series
import underdog.plots.Options
import underdog.plots.charts.Line
import underdog.plots.dsl.series.LineSeries

class CustomLineChart extends Line {
    @NamedVariant
    Line create(
        Series xSeries,
        Series indexSeries,
        Series vixSeries,
        @NamedParam(required = false, value = 'title') String chartTitle = '',
        @NamedParam(required = false, value = 'subtitle') String chartSubtitle = '',
        @NamedParam(required = false) String indexSeriesLabel = ''
    ) {
        Options options = create {
            title {
                text(chartTitle)
                subtext(chartSubtitle)
                left("center")
            }
            legend {
                right("center")
                bottom("2%")
            }
            tooltip {
                trigger("axis")
            }
            axisPointer {
                link {
                    xAxisIndex('all')
                }
            }
            yAxis {
                scale(true)
                name(indexSeriesLabel)
                nameLocation("center")
                nameGap(50)
                boundaryGap(["0%", "5%"])
                gridIndex(0)
                splitNumber(16)
                splitLine {
                    show(true)
                    lineStyle {
                        type("dashed")
                    }
                }
            }
            yAxis {
                scale(true)
                gridIndex(1)
                boundaryGap(["0%", "5%"])
                splitLine {
                    show(true)
                    lineStyle {
                        type("dashed")
                    }
                }
            }
            grid {
                bottom("35%")
            }
            grid {
                top("70%")
            }
            xAxis {
                data(xSeries.toList())
                axisLabel {
                    show(false)
                }
                axisLine {
                    show(false)
                }
                splitLine {
                    show(true)
                    lineStyle {
                        type("dashed")
                    }
                }
            }
            xAxis {
                data(xSeries.toList())
                gridIndex(1)
                nameLocation("center")
                nameTextStyle {
                    padding(10)
                }
                splitLine {
                    show(true)
                    lineStyle {
                        type("dashed")
                    }
                }
            }
            series(LineSeries) {
                name("INDEX")
                data(indexSeries as List)
            }
            series(LineSeries) {
                name("VIX")
                data(vixSeries as List)
                yAxisIndex(1)
                xAxisIndex(1)
            }
        }

        return new Line(options)
    }
}