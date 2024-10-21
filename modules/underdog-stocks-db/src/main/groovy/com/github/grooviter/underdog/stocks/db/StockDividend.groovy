package com.github.grooviter.underdog.stocks.db

import com.github.grooviter.underdog.stocks.db.common.HasSymbol

import java.time.LocalDate

class StockDividend extends HasSymbol {
    LocalDate payDate
    LocalDate exDate
    BigDecimal annualYield
    BigDecimal annualYieldPercent
}
