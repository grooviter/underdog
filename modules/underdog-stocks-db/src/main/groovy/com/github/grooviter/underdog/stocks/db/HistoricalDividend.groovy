package com.github.grooviter.underdog.stocks.db

import com.github.grooviter.underdog.db.Table
import com.github.grooviter.underdog.stocks.db.common.Historical

@Table("historical_dividend")
class HistoricalDividend extends Historical {
    BigDecimal adjDividend
}
