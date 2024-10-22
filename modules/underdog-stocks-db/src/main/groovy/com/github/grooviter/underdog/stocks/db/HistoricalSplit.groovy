package com.github.grooviter.underdog.stocks.db

import com.github.grooviter.underdog.db.Table
import com.github.grooviter.underdog.stocks.db.common.Historical

@Table("historical_split")
class HistoricalSplit extends Historical {
    BigDecimal numerator
    BigDecimal denominator
}
