package underdog.guide.ta

import org.ta4j.core.AnalysisCriterion
import org.ta4j.core.BaseStrategy
import org.ta4j.core.Rule
import org.ta4j.core.Strategy
import org.ta4j.core.TradingRecord
import org.ta4j.core.backtest.BarSeriesManager
import org.ta4j.core.criteria.PositionsRatioCriterion
import org.ta4j.core.criteria.ReturnOverMaxDrawdownCriterion
import org.ta4j.core.criteria.VersusEnterAndHoldCriterion
import org.ta4j.core.criteria.pnl.ReturnCriterion
import org.ta4j.core.indicators.SMAIndicator
import org.ta4j.core.indicators.helpers.ClosePriceIndicator
import org.ta4j.core.num.Num
import org.ta4j.core.rules.CrossedDownIndicatorRule
import org.ta4j.core.rules.CrossedUpIndicatorRule
import org.ta4j.core.rules.StopGainRule
import org.ta4j.core.rules.StopLossRule
import spock.lang.Specification
import underdog.Underdog

class GettingStartedSpec extends Specification {
    def "getting started"() {
        setup:
        // tag::loading_data[]
        def filePath = "src/test/resources/data/ta/stock_quotes_10_years.csv" // <1>
        def dateFormat = "yyyy-MM-dd HH:mm:ss+00:00" // <2>

        def quotes = Underdog.df().read_csv(filePath, dateFormat: dateFormat)
        // end::loading_data[]
        println(quotes)

        // tag::renaming_cols[]
        quotes = quotes
            .drop("Adj Close") // <1>
            .renameSeries(fn: String::toUpperCase) // <2>
        // end::renaming_cols[]

        // tag::convert_to_bar_series[]
        def quotesBarSeries = quotes.toBarSeries()
        // end::convert_to_bar_series[]


        // tag::building_indicators[]
        // Using the close price indicator as root indicator...
        def closePrice = quotesBarSeries.closePriceIndicator
        // Getting the simple moving average (SMA) of the close price over the last 5 bars
        SMAIndicator shortSma = closePrice.sma(5)

        // Here is the 5-bars-SMA value at the 42nd index
        println("5-bars-SMA value at the 42nd index: " + shortSma.getValue(42).doubleValue())

        // Getting a longer SMA (e.g. over the 30 last bars)
        SMAIndicator longSma = closePrice.sma(30)
        // end::building_indicators[]

        // tag::strategy_base[]
        // Buying rules
        // We want to buy:
        //  - if the 5-bars SMA crosses over 30-bars SMA
        //  - or if the price goes below a defined price (e.g $800.00)
        Rule buyingRule = shortSma.xUp(longSma)
            .or(closePrice.xDown(800))

        // Selling rules
        // We want to sell:
        //  - if the 5-bars SMA crosses under 30-bars SMA
        //  - or if the price loses more than 3%
        //  - or if the price earns more than 2%
        Rule sellingRule = shortSma.xDown(longSma)
            .or(closePrice.stopLoss(3))
            .or(closePrice.stopGain(2))

        // Create the strategy
        Strategy strategy = new BaseStrategy(buyingRule, sellingRule)
        // end::strategy_base[]

        // tag::strategy_or[]
        buyingRule = shortSma.xUp(longSma)
            | closePrice.xDown(800)

        sellingRule = sellingRule = shortSma.xDown(longSma)
            | closePrice.stopLoss(3)
            | closePrice.stopGain(2)
        // end::strategy_or[]

        // tag::backtesting_base[]
        // Running our juicy trading strategy...
        BarSeriesManager seriesManager = new BarSeriesManager(quotesBarSeries)
        TradingRecord tradingRecord = seriesManager.run(strategy)
        println("Number of positions (trades) for our strategy: " + tradingRecord.getPositionCount())
        // end::backtesting_base[]

        // tag::backtesting_extension[]
        tradingRecord = quotesBarSeries.run(buyingRule, sellingRule)
        // end::backtesting_extension[]

        // tag::analysis_base[]
        // Getting the winning positions ratio
        AnalysisCriterion winningPositionsRatio = new PositionsRatioCriterion(AnalysisCriterion.PositionFilter.PROFIT)
        println("Winning positions ratio: " + winningPositionsRatio.calculate(quotesBarSeries, tradingRecord))

        // Getting a risk-reward ratio
        AnalysisCriterion romad = new ReturnOverMaxDrawdownCriterion()
        println("Return over Max Drawdown: " + romad.calculate(quotesBarSeries, tradingRecord))

        // Total return of our strategy vs total return of a buy-and-hold strategy
        AnalysisCriterion vsBuyAndHold = new VersusEnterAndHoldCriterion(new ReturnCriterion())
        println("Our return vs buy-and-hold return: " + vsBuyAndHold.calculate(quotesBarSeries, tradingRecord))
        // end::analysis_base[]

        expect:
        (0.54..0.55).containsWithinBounds(winningPositionsRatio.calculate(quotesBarSeries, tradingRecord).doubleValue())
        (3.45..3.46).containsWithinBounds(romad.calculate(quotesBarSeries, tradingRecord).doubleValue())
        (0.004..0.005).containsWithinBounds(vsBuyAndHold.calculate(quotesBarSeries, tradingRecord).doubleValue())
    }
}
