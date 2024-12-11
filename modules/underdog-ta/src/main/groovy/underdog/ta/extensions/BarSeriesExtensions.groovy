package underdog.ta.extensions

import org.ta4j.core.BarSeries
import org.ta4j.core.BaseStrategy
import org.ta4j.core.Rule
import org.ta4j.core.Strategy
import org.ta4j.core.TradingRecord
import org.ta4j.core.backtest.BarSeriesManager
import org.ta4j.core.backtest.TradeExecutionModel
import org.ta4j.core.indicators.helpers.ClosePriceIndicator
import org.ta4j.core.indicators.helpers.DateTimeIndicator
import org.ta4j.core.rules.DayOfWeekRule

import java.time.DayOfWeek

/**
 * Adds functions to {@link BarSeries} instances
 *
 * @since 0.1.0
 */
class BarSeriesExtensions {

    /**
     * Creates a {@link ClosePriceIndicator} out of a {@link BarSeries}
     *
     * @param source {@link BarSeries} to create the {@link ClosePriceIndicator} from
     * @return an instance of {@link ClosePriceIndicator}
     * @since 0.1.0
     */
    static ClosePriceIndicator getClosePriceIndicator(BarSeries source) {
        return new ClosePriceIndicator(source)
    }

    /**
     * Creates a {@link DayOfWeekRule} out of a {@link BarSeries}
     *
     * @param source the {@link BarSeries} to get the {@link DayOfWeekRule} from
     * @param days a range of {@link DayOfWeek}
     * @return an instance of {@link DayOfWeekRule}
     * @since 0.1.0
     */
    static DayOfWeekRule dayOfWeekRule(BarSeries source, Range<DayOfWeek> days) {
        new DayOfWeekRule(new DateTimeIndicator(source), days as DayOfWeek[])
    }

    /**
     * Executes a {@link BaseStrategy} returning a {@link TradingRecord}
     *
     * @param source the {@link BarSeries} used for the trading record
     * @param entryRule the entry {@link Rule}
     * @param exitRule the exit {@link Rule}
     * @param model (Optional) you can use a specific {@link TradeExecutionModel}
     * @return an instance of {@link TradingRecord}
     * @since 0.1.0
     */
    static TradingRecord run(BarSeries source, Rule entryRule, Rule exitRule, TradeExecutionModel model = null) {
        Strategy strategy = new BaseStrategy(entryRule, exitRule)

        if (!model) {
            return new BarSeriesManager(source).run(strategy)
        }

        return new BarSeriesManager(source, model).run(strategy)
    }
}
