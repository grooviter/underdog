package com.github.grooviter.underdog.stocks.db

import com.github.grooviter.underdog.stocks.db.common.Historical


class HistoricalQuote extends Historical {
    BigDecimal open
    BigDecimal low
    BigDecimal high
    BigDecimal close
    BigDecimal adjClose
    Long volume
}
