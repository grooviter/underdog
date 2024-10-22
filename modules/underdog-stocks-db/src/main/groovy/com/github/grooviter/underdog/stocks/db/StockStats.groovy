package com.github.grooviter.underdog.stocks.db

import com.github.grooviter.underdog.db.Table
import com.github.grooviter.underdog.stocks.db.common.HasSymbol

import java.time.LocalDate

@Table("stock_stats")
class StockStats extends HasSymbol {
    BigDecimal marketCap
    Long sharesFloat
    Long sharesOutstanding
    Long sharesOwned
    BigDecimal eps
    BigDecimal pe
    BigDecimal peg
    BigDecimal epsEstimateCurrentYear
    BigDecimal epsEstimateNextQuarter
    BigDecimal epsEstimateNextYear
    BigDecimal priceBook
    BigDecimal priceSales
    BigDecimal bookValuePerShare
    BigDecimal revenue // ttm
    BigDecimal ebitda // ttm
    BigDecimal oneYearTargetPrice
    BigDecimal shortRatio
    LocalDate earningsAnnouncement
}
