package underdog.ta.extensions

import org.ta4j.core.BarSeries
import org.ta4j.core.indicators.helpers.ClosePriceIndicator
import underdog.Underdog
import underdog.ta.BaseSpec

import java.time.LocalDate
import java.time.LocalDateTime

class DataframeExtensionsSpec extends BaseSpec {

    def "DataFrame to BarSeries"() {
        setup:
        BarSeries series = createBarSeries()

        expect:
        series.barCount == 2517
    }

    def "Indicator to Series"() {
        setup: "dataframe"
        def tickers = createDataFrame()

        and: "ta series"
        def series = tickers.toBarSeries()

        when: "indicators"
        def closePrice = new ClosePriceIndicator(series)
        def avg14 = closePrice.ema(14)
        def sd14 = closePrice.sd(14)

        and: "bollinger bands"
        def middle = avg14.bollingerMiddle()
        def lower = middle.bollingerLower(sd14)
        def upper = middle.bollingerUpper(sd14)

        and: "preparing data to visualize"
        def plotDataFrame = [
            middle: middle.toSeries(),
            lower: lower.toSeries(),
            upper: upper.toSeries(),
            X: tickers['Date'](LocalDateTime, String, dateFormat('dd/MM/yyyy'))
        ].toDataFrame("plot dataframe")

        then:
        Underdog.plots()
            .lines(
                plotDataFrame,
                title: "Nvidia bollinger bands",
                xLabel: "Date",
                yLabel: "Price")
            .addMarkAreaInY(200, 220, color: "rgba(180, 183, 198, 0.3)", title: "Increase")
            .customize {
                legend {
                    show(true)
                    top("10%")
                }
                tooltip {
                    trigger("axis")
                }
            }
            .show()
    }

    private static Closure<String> dateFormat(String format) {
        return { LocalDate dateTime ->
            return dateTime.format(format)
        }
    }
}
