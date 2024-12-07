package underdog.ta.extensions

import org.ta4j.core.BarSeries
import org.ta4j.core.BaseStrategy
import org.ta4j.core.Trade
import org.ta4j.core.TradingRecord
import org.ta4j.core.backtest.BarSeriesManager
import org.ta4j.core.backtest.TradeOnCurrentCloseModel
import org.ta4j.core.indicators.helpers.*
import org.ta4j.core.rules.DayOfWeekRule
import underdog.plots.Plots
import underdog.ta.BaseSpec
import underdog.ta.CustomLineChart

import java.time.DayOfWeek
import java.time.LocalDate
import java.time.ZoneId

class StrategiesExtensionsSpec extends BaseSpec {
    def "create strategy with extensions"() {
        setup:
        def series = createBarSeries()

        and: "source indicators"
        def closePrice = new ClosePriceIndicator(series)

        and: "creating buy rule"
        def buyRule = closePrice.with {
            sma(5).xUp(sma(20))
                | stopLoss(3)
                | stopGain(2)
        }

        and: "creating sell rule"
        def sellRule = closePrice.with {
            sma(5).xDown(190)
                | stopGain(3)
                | xDown(190)
        }

        and: "creating strategy"
        def strategy = new BaseStrategy(buyRule, sellRule)

        when: "back-testing"
        TradingRecord tradingRecord = new BarSeriesManager(series).run(strategy)

        then: "several entries"
        tradingRecord.positionCount > 0

        when:
        def df = createDataFrame()
            .loc[__, ['Date', 'Close']]
            .rename(mapper: [Date: 'X'])

        df['X'] = df['X'](LocalDate, String) { it.format("dd/MM/yyyy") }

        def options = Plots
            .plots()
            .lines(df, title: "Trading Strategy", xLabel: "Dates", yLabel: "Closing price")

        and: "adding buy/sell points"
        tradingRecord.trades.each { Trade trade ->
            String x = series.getBar(trade.index).beginTime.format('dd/MM/yyyy')
            double y = trade.value.doubleValue()
            String t = trade.type.name().find()
            options.addAnnotation(x, y, text: t, color: t == 'B' ? 'green' : 'red')
        }

        then: "we should see when buy and sell happened"
        options
            .customize {
                legend {
                    show(true)
                }
                tooltip {
                    trigger('axis')
                }
            }
            .show()
    }

    def "custom line chart"() {
        setup: "building data"
        def sp500 = SP500Data
            .loc[__, ['Close/Last', 'Date']]
            .rename(mapper: ['Close/Last': 'Close'])
            .rename(fn: String.&toUpperCase)

        def merged = sp500
            .merge(vixData, on: ['DATE'], allowDuplicateColumns: true)
            .loc[__, ['DATE', 'CLOSE', 'T2.CLOSE']]

        and: "preparing x axis data"
        merged['DATE'] = merged['DATE'](LocalDate, String) {
            it.format("dd/MM/yyyy")
        }

        expect:
        new CustomLineChart()
            .create(
                merged['DATE'],
                merged['CLOSE'],
                merged['T2.CLOSE'],
                title: "SP500 - VIX (2024)")
            .show()
    }

    def "trading the vix: market is down and waiting to enter"() {
        setup:
        BarSeries vixSeries = vixData.sort_values(by: ['DATE']).toBarSeries(ZoneId.of("US/Eastern"))
        BarSeries nvidaSeries = createDataFrame().rename(fn: String.&toUpperCase).toBarSeries()

        and: "running vix strategy"
        def vixPriceIndicator = new ClosePriceIndicator(vixSeries)
        def duringWeekdays = new DayOfWeekRule(new DateTimeIndicator(vixSeries), DayOfWeek.MONDAY..DayOfWeek.FRIDAY as DayOfWeek[])

        def buyRule = vixPriceIndicator
            .roc(7)
            .xUp(50) & duringWeekdays
        def sellRule = vixPriceIndicator
            .roc(7)
            .xDown(10) & duringWeekdays
        def strategy = new BaseStrategy(buyRule, sellRule)

        def tradingRecord = new BarSeriesManager(vixSeries, new TradeOnCurrentCloseModel()).run(strategy)

        when: "comparing the vix with the strategy"
        def df = vixData
            .sort_values(by: ['DATE']).loc[__, ['DATE', 'CLOSE']]
            .rename(mapper: [DATE: 'X', CLOSE: 'VIX'])

        and: "showing just 2020-2024"
        df = df[
            df['X'] > LocalDate.parse("01/01/2020", "dd/MM/yyyy") &
            df['X'] < LocalDate.parse('31/12/2024', "dd/MM/yyyy")
        ]

        df['X'] = df['X'](LocalDate, String) { it.format("dd/MM/yyyy") }

        and: "creating plot"
        def plot = Plots.plots().lines(df)

        and: "adding buy/sell trades"
        tradingRecord.trades.each { Trade trade ->
            String x = vixSeries.getBar(trade.index).endTime.format('dd/MM/yyyy')
            double y = trade.value.doubleValue()
            String t = trade.type.name().find()
            plot.addAnnotation(x, y, text: t, color: t == 'B' ? 'green' : 'red')
        }

        then: "show plot"
        plot.customize {
            tooltip {
                trigger("axis")
            }
        }.show()
    }

    def "trading the vix and stock price"() {
        setup:
        def vixDataFrame = vixData.sort_values(by: ['DATE'])
        def vixSeries = vixDataFrame.toBarSeries(ZoneId.of("US/Eastern"))

        and: "vix"
        def vixIndicator = new ClosePriceIndicator(vixSeries)
        def duringWeekdays = new DayOfWeekRule(new DateTimeIndicator(vixSeries), DayOfWeek.MONDAY..DayOfWeek.FRIDAY as DayOfWeek[])
        def buyRule = vixIndicator.roc(7).xUp(20) & duringWeekdays
        def sellRule = vixIndicator.roc(7).xDown(5) & duringWeekdays

        and:
        def nvda = createDataFrame().rename(fn: String.&toUpperCase)
        def nvdaSeries = nvda.toBarSeries()
        def strategy = new BaseStrategy(buyRule, sellRule)
        def tradingRecord = new BarSeriesManager(nvdaSeries, new TradeOnCurrentCloseModel()).run(strategy)

        nvda = nvda[
            nvda['DATE'] > LocalDate.parse("01/07/2021", "dd/MM/yyyy") &
            nvda['DATE'] < LocalDate.parse('01/08/2023', "dd/MM/yyyy")
        ]

        def plot = new CustomLineChart()
            .create(
                nvda['DATE'](LocalDate, String) { it.format("dd/MM/yyyy") },
                nvda['CLOSE'],
                vixDataFrame['CLOSE'],
                title: "Trading Nvidia only using the VIX",
                subtitle: "buying xup 20% / selling xup 10%")

        tradingRecord.trades.each { Trade trade ->
            String x = nvdaSeries.getBar(trade.index).endTime.format('dd/MM/yyyy')
            double y = trade.value.doubleValue()
            String t = trade.type.name().find()
            plot.addAnnotation(x, y, text: t, color: t == 'B' ? 'rgb(145, 203, 116)' : 'rgb(203, 116, 116, 0.8)')
        }

        expect:
        Plots.show(plot, height: "600px", width: "800px", theme: "dark")
    }
}
