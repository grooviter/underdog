package com.github.grooviter.underdog.finance.technical

import org.ta4j.core.BarSeries
import org.ta4j.core.BaseStrategy
import org.ta4j.core.Rule
import org.ta4j.core.Strategy
import org.ta4j.core.indicators.StochasticOscillatorKIndicator
import org.ta4j.core.indicators.helpers.ClosePriceIndicator

class Strategies {

    static ClosePriceIndicator closePrice(BarSeries series) {
        return new ClosePriceIndicator(series)
    }

    static StochasticOscillatorKIndicator stochasticOK(BarSeries series, Integer barCount) {
        return new StochasticOscillatorKIndicator(series, barCount)
    }

    static Strategy from(Rule entryRule, Rule exitRule) {
        return new BaseStrategy(entryRule, exitRule)
    }
}
