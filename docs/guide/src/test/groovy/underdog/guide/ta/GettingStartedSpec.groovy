package underdog.guide.ta

import org.ta4j.core.AnalysisCriterion
import org.ta4j.core.BaseStrategy
import org.ta4j.core.Position
import org.ta4j.core.Rule
import org.ta4j.core.Strategy
import org.ta4j.core.TradingRecord
import org.ta4j.core.backtest.BarSeriesManager
import org.ta4j.core.criteria.PositionsRatioCriterion
import org.ta4j.core.criteria.ReturnOverMaxDrawdownCriterion
import org.ta4j.core.criteria.VersusEnterAndHoldCriterion
import org.ta4j.core.criteria.pnl.ReturnCriterion
import org.ta4j.core.indicators.SMAIndicator
import spock.lang.Specification
import underdog.Underdog
import underdog.plots.Plots

import java.time.LocalDate

class GettingStartedSpec extends Specification {
    def "getting started"() {
        setup:
        // --8<-- [start:loading_data]
        // The file path where the csv file can be found
        def filePath = "src/test/resources/data/ta/stock_quotes_10_years.csv"

        // The format of the dates included in the file
        def dateFormat = "yyyy-MM-dd HH:mm:ss+00:00"

        def quotes = Underdog.df().read_csv(filePath, dateFormat: dateFormat)
        // --8<-- [end:loading_data]
        println(quotes)

        // --8<-- [start:renaming_cols]
        quotes = quotes
            .drop("Adj Close") // Removing the "Adj Close" series
            .renameSeries(fn: String::toUpperCase) // Renaming all remaining columns to upper case to match
        // --8<-- [end:renaming_cols]

        // --8<-- [start:convert_to_bar_series]
        def quotesBarSeries = quotes.toBarSeries()
        // --8<-- [end:convert_to_bar_series]


        // --8<-- [start:building_indicators]
        // Using the close price indicator as root indicator...
        def closePrice = quotesBarSeries.closePriceIndicator
        // Getting the simple moving average (SMA) of the close price over the last 5 bars
        SMAIndicator shortSma = closePrice.sma(5)

        // Here is the 5-bars-SMA value at the 42nd index
        println("5-bars-SMA value at the 42nd index: " + shortSma.getValue(42).doubleValue())

        // Getting a longer SMA (e.g. over the 30 last bars)
        SMAIndicator longSma = closePrice.sma(30)
        // --8<-- [end:building_indicators]

        // --8<-- [start:strategy_base]
        // Buying rules
        // We want to buy:
        //  - if the 5-bars SMA crosses over 30-bars SMA
        //  - or if the price goes below a defined price (e.g $800.00)
        Rule buyingRule = shortSma.xUp(longSma)
            .or(closePrice.xDown(120))

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
        // --8<-- [end:strategy_base]

        // --8<-- [start:strategy_or]
        buyingRule = shortSma.xUp(longSma)
            | closePrice.xDown(120)

        sellingRule = shortSma.xDown(longSma)
            | closePrice.stopLoss(3)
            | closePrice.stopGain(2)
        // --8<-- [end:strategy_or]

        // --8<-- [start:backtesting_base]
        // Running our juicy trading strategy...
        BarSeriesManager seriesManager = new BarSeriesManager(quotesBarSeries)
        TradingRecord tradingRecord = seriesManager.run(strategy)
        println("Number of positions (trades) for our strategy: " + tradingRecord.getPositionCount())
        // --8<-- [end:backtesting_base]

        // --8<-- [start:backtesting_extension]
        tradingRecord = quotesBarSeries.run(buyingRule, sellingRule)
        // --8<-- [end:backtesting_extension]

        // --8<-- [start:show_trades]
        // getting only stocks from 2024-04-01
        quotes = quotes[quotes['DATE'] >= LocalDate.parse('2024-04-01')]

        // getting Underdog's series for x and y coordinates
        def xs = quotes['DATE'](LocalDate, String) { it.format("dd/MM/yyyy") }
        def ys = quotes['CLOSE']

        // building a line plot
        def plot = Underdog.plots()
            .line(
                xs.rename("Dates"),
                ys.rename("Closing Price"),
                title: "Trades from 2024-01-01",
                subtitle: "Using Underdog's TA and Ta4j")

        // showing trades over stock quotes
        tradingRecord.trades.each {trade ->
            String x = quotesBarSeries.getBar(trade.index).endTime.format('dd/MM/yyyy')
            double y = trade.value.doubleValue()
            String t = trade.type.name()[0] // first letter of type name ('B' for buy, 'S' for sell)
            plot.addAnnotation(x, y, text: t, color: t == 'B' ? 'green' : '#dd7474')
        }

        // showing the plot
        plot.show()
        // --8<-- [end:show_trades]
        Plots.show(plot, theme: "dark")

        // --8<-- [start:show_positions]
        // creating a function to map every position to a map containing date and profit
        def positionToDataFrameEntry = { Position pos ->
            return [
                date: quotesBarSeries.getBar(pos.exit.index).endTime.toLocalDate(),
                profit: pos.grossProfit.doubleValue()
            ]
        }

        // getting gross profit and date of every position
        def wins = tradingRecord
            .positions
            .collect(positionToDataFrameEntry)
            .toDataFrame("trade values")

        // mark every position as winners/losers
        wins['winner'] = wins['profit'](Double, String) { it > 0 ? "winners" : "losers" }

        // grouping by winners/losers and get the count
        def byMonth = wins
            .agg(profit: 'count')
            .by('winner')
            .renameSeries(mapper: ["count [profit]": "value", winner: "name"])

        // getting the min/max date for the subtitle of the plot
        def minDate = wins['date'].min(LocalDate).format("dd/MM/yyyy")
        def maxDate = wins['date'].max(LocalDate).format("dd/MM/yyyy")

        // building the plot
        def piePlot = Underdog
            .plots()
            .pie(
                byMonth['name'].toList(),
                byMonth['value'].toList(),
                title: "Winners vs Losers positions",
                subtitle: "From ${minDate} to ${maxDate}"
            )

        // showing the plot
        piePlot.show()
        // --8<-- [end:show_positions]
        Plots.show(piePlot, theme: "dark")

        // --8<-- [start:analysis_base]
        // Getting the winning positions ratio
        AnalysisCriterion winningPositionsRatio = new PositionsRatioCriterion(AnalysisCriterion.PositionFilter.PROFIT)
        double winningPositionRatioValue = winningPositionsRatio.calculate(quotesBarSeries, tradingRecord).doubleValue()
        println("Winning positions ratio: " + winningPositionRatioValue)

        // Getting a risk-reward ratio
        AnalysisCriterion romad = new ReturnOverMaxDrawdownCriterion()
        double nomadValue = romad.calculate(quotesBarSeries, tradingRecord).doubleValue()
        println("Return over Max Drawdown: " + nomadValue)

        // Total return of our strategy vs total return of a buy-and-hold strategy
        AnalysisCriterion vsBuyAndHold = new VersusEnterAndHoldCriterion(new ReturnCriterion())
        double vsBuyAndHoldValue = vsBuyAndHold.calculate(quotesBarSeries, tradingRecord).doubleValue()
        println("Our return vs buy-and-hold return: " + vsBuyAndHoldValue)
        // --8<-- [end:analysis_base]

        // --8<-- [start:analysis_radar]
        def winningRatioPlot = Underdog.plots()
            .radar(
                // names of metrics
                ['winning ratio', 'return over drawdown', 'return vs buy-and-hold'],
                // maximum possible value of each metric
                [1, 100, 1],
                // values from metrics
                [winningPositionRatioValue, nomadValue, vsBuyAndHoldValue],
                title: "Metrics comparison",
                subtitle: "Winning Ratio / Risk Reward Ratio / Return vs Buy-And-Hold"
            )
        winningRatioPlot.show()
        // --8<-- [end:analysis_radar]
        Plots.show(winningRatioPlot, theme: "dark")

        expect:
        (0.53..0.55).containsWithinBounds(winningPositionRatioValue)
        (3.05..3.46).containsWithinBounds(nomadValue)
        (0.003..0.005).containsWithinBounds(vsBuyAndHoldValue)
    }
}
