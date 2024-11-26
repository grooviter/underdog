package memento.plots.charts

import com.github.grooviter.underdog.plots.Options

class Chart {

    Options createGridOptions(String chartTitle, String chartSubtitle = '') {
        return Options.create {
            grid {
                width("70%")
                left("15%")
                right("15%")
                height("70%")
                top("15%")
                bottom("15%")
            }
            title {
                text chartTitle
                subtext(chartSubtitle)
                top("3%")
                left("center")
            }
        }
    }

    Options createXAxisOptions(String xLabel, List<Number> xs = []) {
        return Options.create {
            xAxis {
                scale(true)
                name(xLabel)
                nameLocation("center")
                nameTextStyle {
                    padding(25)
                }
                if (xs) {
                    data(xs)
                }
            }
        }
    }

    Options createYAxisOptions(String yLabel) {
        return Options.create {
            yAxis {
                scale(true)
                name(yLabel)
                nameLocation("center")
                nameTextStyle {
                    padding(30)
                }
            }
        }
    }
}
