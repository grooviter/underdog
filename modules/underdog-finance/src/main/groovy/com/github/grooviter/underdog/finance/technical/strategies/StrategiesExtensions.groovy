package com.github.grooviter.underdog.finance.technical.strategies

import org.ta4j.core.Indicator
import org.ta4j.core.indicators.EMAIndicator
import org.ta4j.core.indicators.MACDIndicator
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

class StrategiesExtensions {

    static CrossedUpIndicatorRule xUp(Indicator left, Indicator right) {
        return new CrossedUpIndicatorRule(left, right)
    }

    static CrossedUpIndicatorRule xUp(Indicator left, Number threshold) {
        return new CrossedUpIndicatorRule(left, threshold)
    }

    static CrossedDownIndicatorRule xDown(Indicator indicator, Number threshold) {
        return new CrossedDownIndicatorRule(indicator, threshold)
    }

    static OverIndicatorRule over(Indicator indicator, Indicator other) {
        return new OverIndicatorRule(indicator, other)
    }

    static UnderIndicatorRule under(Indicator indicator, Indicator other) {
        return new UnderIndicatorRule(indicator, other)
    }

    static SMAIndicator sma(Indicator indicator, Integer barCount) {
        return new SMAIndicator(indicator, barCount)
    }

    static EMAIndicator ema(Indicator indicator, Integer barCount) {
        return new EMAIndicator(indicator, barCount)
    }

    static MACDIndicator macd(Indicator indicator, Integer shortBarCount, Integer longBarCount) {
        return new MACDIndicator(indicator, shortBarCount, longBarCount)
    }

    static StandardDeviationIndicator sd(Indicator indicator, Integer barCount) {
        return new StandardDeviationIndicator(indicator, barCount)
    }

    static BollingerBandsMiddleIndicator bollingerMiddle(Indicator indicator) {
        return new BollingerBandsMiddleIndicator(indicator)
    }

    static BollingerBandsLowerIndicator bollingerLower(BollingerBandsMiddleIndicator middle, Indicator indicator) {
        return new BollingerBandsLowerIndicator(middle, indicator)
    }

    static BollingerBandsUpperIndicator bollingerUpper(BollingerBandsMiddleIndicator middle, Indicator indicator) {
        return new BollingerBandsUpperIndicator(middle, indicator)
    }

    static StopLossRule loses(ClosePriceIndicator price, Number percentage) {
        return new StopLossRule(price, percentage)
    }

    static StopGainRule earns(ClosePriceIndicator price, Number percentage) {
        return new StopGainRule(price, percentage)
    }
}
