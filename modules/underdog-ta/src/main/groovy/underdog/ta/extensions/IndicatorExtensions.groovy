package underdog.ta.extensions

import org.ta4j.core.Indicator
import org.ta4j.core.indicators.EMAIndicator
import org.ta4j.core.indicators.MACDIndicator
import org.ta4j.core.indicators.ROCIndicator
import org.ta4j.core.indicators.SMAIndicator
import org.ta4j.core.indicators.bollinger.BollingerBandsLowerIndicator
import org.ta4j.core.indicators.bollinger.BollingerBandsMiddleIndicator
import org.ta4j.core.indicators.bollinger.BollingerBandsUpperIndicator
import org.ta4j.core.indicators.helpers.ClosePriceIndicator
import org.ta4j.core.indicators.statistics.StandardDeviationIndicator
import org.ta4j.core.rules.CrossedDownIndicatorRule
import org.ta4j.core.rules.CrossedUpIndicatorRule
import org.ta4j.core.rules.OverIndicatorRule
import org.ta4j.core.rules.StopGainRule
import org.ta4j.core.rules.StopLossRule
import org.ta4j.core.rules.UnderIndicatorRule

/**
 * Includes functions to extend {@link Indicator} functionality
 *
 * @since 0.1.0
 */
class IndicatorExtensions {

    /**
     * Indicates that the condition happens when the indicator crosses up the right
     * indicator passed as parameter
     *
     * @param indicator the reference indicator
     * @param right the indicator to cross over
     * @return a {@link CrossedUpIndicatorRule} instance
     * @since 0.1.0
     */
    static CrossedUpIndicatorRule xUp(Indicator indicator, Indicator right) {
        return new CrossedUpIndicatorRule(indicator, right)
    }

    /**
     * @param indicator
     * @param threshold
     * @return
     * @since 0.1.0
     */
    static CrossedUpIndicatorRule xUp(Indicator indicator, Number threshold) {
        return new CrossedUpIndicatorRule(indicator, threshold)
    }

    /**
     * @param indicator
     * @param threshold
     * @return
     * @since 0.1.0
     */
    static CrossedDownIndicatorRule xDown(Indicator indicator, Number threshold) {
        return new CrossedDownIndicatorRule(indicator, threshold)
    }

    /**
     * @param left
     * @param right
     * @return
     * @since 0.1.0
     */
    static CrossedDownIndicatorRule xDown(Indicator left, Indicator right) {
        return new CrossedDownIndicatorRule(left, right)
    }

    /**
     * @param left
     * @param right
     * @return
     * @since 0.1.0
     */
    static OverIndicatorRule over(Indicator left, Indicator right) {
        return new OverIndicatorRule(left, right)
    }

    /**
     * @param left
     * @param right
     * @return
     * @since 0.1.0
     */
    static UnderIndicatorRule under(Indicator left, Indicator right) {
        return new UnderIndicatorRule(left, right)
    }

    /**
     * @param indicator
     * @param barCount
     * @return
     * @since 0.1.0
     */
    static SMAIndicator sma(Indicator indicator, Integer barCount) {
        return new SMAIndicator(indicator, barCount)
    }

    /**
     * @param indicator
     * @param barCount
     * @return
     * @since 0.1.0
     */
    static EMAIndicator ema(Indicator indicator, Integer barCount) {
        return new EMAIndicator(indicator, barCount)
    }

    /**
     * @param indicator
     * @param barCount
     * @return
     * @since 0.1.0
     */
    static ROCIndicator roc(Indicator indicator, Integer barCount) {
        return new ROCIndicator(indicator, barCount)
    }

    /**
     * @param indicator
     * @param shortBarCount
     * @param longBarCount
     * @return
     * @since 0.1.0
     */
    static MACDIndicator macd(Indicator indicator, Integer shortBarCount, Integer longBarCount) {
        return new MACDIndicator(indicator, shortBarCount, longBarCount)
    }

    /**
     * @param indicator
     * @param barCount
     * @return
     * @since 0.1.0
     */
    static StandardDeviationIndicator sd(Indicator indicator, Integer barCount) {
        return new StandardDeviationIndicator(indicator, barCount)
    }

    /**
     * @param indicator
     * @return
     * @since 0.1.0
     */
    static BollingerBandsMiddleIndicator bollingerMiddle(Indicator indicator) {
        return new BollingerBandsMiddleIndicator(indicator)
    }

    /**
     * @param middle
     * @param indicator
     * @return
     * @since 0.1.0
     */
    static BollingerBandsLowerIndicator bollingerLower(BollingerBandsMiddleIndicator middle, Indicator indicator) {
        return new BollingerBandsLowerIndicator(middle, indicator)
    }

    /**
     * @param middle
     * @param indicator
     * @return
     * @since 0.1.0
     */
    static BollingerBandsUpperIndicator bollingerUpper(BollingerBandsMiddleIndicator middle, Indicator indicator) {
        return new BollingerBandsUpperIndicator(middle, indicator)
    }

    /**
     * @param price
     * @param percentage
     * @return
     * @since 0.1.0
     */
    static StopLossRule stopLoss(ClosePriceIndicator price, Number percentage) {
        return new StopLossRule(price, percentage)
    }

    /**
     * @param price
     * @param percentage
     * @return
     * @since 0.1.0
     */
    static CrossedDownIndicatorRule loses(ClosePriceIndicator price, Number percentage) {
        return new ROCIndicator(price, 1).xDown(percentage)
    }

    /**
     * @param price
     * @param percentage
     * @return
     * @since 0.1.0
     */
    static StopGainRule stopGain(ClosePriceIndicator price, Number percentage) {
        return new StopGainRule(price, percentage)
    }

    /**
     * @param price
     * @param percentage
     * @return
     * @since 0.1.0
     */
    static CrossedUpIndicatorRule gains(ClosePriceIndicator price, Number percentage) {
        return new ROCIndicator(price, 1).xUp(percentage)
    }
}
