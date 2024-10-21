package com.github.grooviter.underdog.finance

import com.github.grooviter.underdog.finance.technical.Strategies
import org.ta4j.core.*
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

import java.time.Duration
import java.time.ZonedDateTime

import static com.github.grooviter.underdog.finance.technical.Strategies.closePrice
import static com.github.grooviter.underdog.finance.technical.Strategies.stochasticOK

class BaseExampleSpec extends Specification {
    def "proof of concept"() {
        setup: "At the beginning we just need a bar series and add some data"
        def bars = (0..100).collect {
            new BaseBar(
                Duration.ofDays(1L),
                ZonedDateTime.now(),
                105.42,
                112.99,
                104.01,
                111.42,
                1337)
        }

        BarSeries series = new BaseBarSeriesBuilder()
                .withName("ASML")
                .withBars(bars)
                .build()

        and: "Using indicators"
        // Getting the close price of the bars
        Num firstClosePrice = series.getBar(0).getClosePrice();
        System.out.println("First close price: " + firstClosePrice.doubleValue());
        // Or within an indicator:
        ClosePriceIndicator closePrice = new ClosePriceIndicator(series);
        // Here is the same close price:
        System.out.println(firstClosePrice.isEqual(closePrice.getValue(0))); // equal to firstClosePrice
        // Getting the simple moving average (SMA) of the close price over the last 5 bars
        SMAIndicator shortSma = new SMAIndicator(closePrice, 5);
        // Here is the 5-bars-SMA value at the 42nd index
        System.out.println("5-bars-SMA value at the 42nd index: " + shortSma.getValue(42).doubleValue());
        // Getting a longer SMA (e.g. over the 30 last bars)
        SMAIndicator longSma = new SMAIndicator(closePrice, 30);

        and: "Building a trading strategy"
        // Buying rules
        // We want to buy:
        //  - if the 5-bars SMA crosses over 30-bars SMA
        //  - or if the price goes below a defined price (e.g $800.00)
        Rule buyingRule = new CrossedUpIndicatorRule(shortSma, longSma)
                .or(new CrossedDownIndicatorRule(closePrice, 800));

        // Selling rules
        // We want to sell:
        //  - if the 5-bars SMA crosses under 30-bars SMA
        //  - or if the price loses more than 3%
        //  - or if the price earns more than 2%
        Rule sellingRule = new CrossedDownIndicatorRule(shortSma, longSma)
                .or(new StopLossRule(closePrice, series.numOf(3)))
                .or(new StopGainRule(closePrice, series.numOf(2)));

        // Create the strategy
        Strategy strategy = new BaseStrategy(buyingRule, sellingRule);

        and: "Backtesting/running our juicy strategy"
        BarSeriesManager seriesManager = new BarSeriesManager(series);
        TradingRecord tradingRecord = seriesManager.run(strategy);
        System.out.println("Number of positions (trades) for our strategy: " + tradingRecord.getPositionCount());

        and: "Analyzing our results"
        // Getting the winning positions ratio
        AnalysisCriterion winningPositionsRatio = new PositionsRatioCriterion(AnalysisCriterion.PositionFilter.PROFIT);
        System.out.println("Winning positions ratio: " + winningPositionsRatio.calculate(series, tradingRecord));

        // Getting a risk-reward ratio
        AnalysisCriterion romad = new ReturnOverMaxDrawdownCriterion();
        System.out.println("Return over Max Drawdown: " + romad.calculate(series, tradingRecord));

        // Total return of our strategy vs total return of a buy-and-hold strategy
        AnalysisCriterion vsBuyAndHold = new VersusEnterAndHoldCriterion(new ReturnCriterion());
        System.out.println("Our return vs buy-and-hold return: " + vsBuyAndHold.calculate(series, tradingRecord));

        expect:
        true
    }

    def "dsl"() {
        setup:
        def series = createBarSeries()

        and: "source indicators"
        def closePrice = new ClosePriceIndicator(series)

        and: "creating buy rule"
        def buyRule = closePrice.with {
            sma(5).xUp(sma(20))
                | loses(3)
                | earns(2)
        }

        and: "creating sell rule"
        def sellRule = closePrice.with {
            sma(5).xDown(20) | xDown(800)
        }

        and: "creating strategy"
        def strategy = new BaseStrategy(buyRule, sellRule)

        when: "back-testing"
        TradingRecord tradingRecord = new BarSeriesManager(series).run(strategy)

        then: "several entries"
        tradingRecord.positionCount > 0
    }

    def "Example"() {
        when:
        def closePrice = closePrice(createBarSeries())
        def sd14 = closePrice.sd(14)
        def ema14 = closePrice.ema(14)

        def (upper, lower) =  ema14.bollingerMiddle().with {
            return [
                bollingerUpper(sd14),
                bollingerLower(sd14)
            ]
        }

        then:
        true
    }

    def "another"() {
        when:
        def series = createBarSeries()
        def closePrice = closePrice(series)
        def shortEma = closePrice.ema(9)
        def longEma = closePrice.ema(26)
        def stochasticOK = stochasticOK(series, 14);
        def macd = closePrice.macd(9, 26)
        def emaMacd = macd.ema(18)

        def entryRule = shortEma.over(longEma)
            & stochasticOK.xDown(20)
            & macd.over(emaMacd)

        def exitRule = shortEma.under(longEma)
             & stochasticOK.xUp(80)
             & macd.under(emaMacd)

        then:
        Strategies.from(entryRule, exitRule)
    }

    def createBarSeries() {
        def bars = (0..100).collect {
            new BaseBar(
                    Duration.ofDays(1L),
                    ZonedDateTime.now(),
                    105.42,
                    112.99,
                    104.01,
                    111.42,
                    1337)
        }

        return new BaseBarSeriesBuilder()
                .withName("ASML")
                .withBars(bars)
                .build()
    }
}