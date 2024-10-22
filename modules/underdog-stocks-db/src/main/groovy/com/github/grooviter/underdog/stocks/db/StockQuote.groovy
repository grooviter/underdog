package com.github.grooviter.underdog.stocks.db

import com.github.grooviter.underdog.db.Table
import com.github.grooviter.underdog.stocks.db.common.HasSymbol

import java.time.LocalDate

@Table("stock_quote")
class StockQuote extends HasSymbol {
    String timeZone
    BigDecimal ask
    Long askSize
    BigDecimal bid
    Long bidSize
    BigDecimal price
    Long lastTradeSize
    String lastTradeDateStr
    String lastTradeTimeStr
    LocalDate lastTradeTime
    BigDecimal open
    BigDecimal previousClose
    BigDecimal dayLow
    BigDecimal dayHigh
    BigDecimal yearLow
    BigDecimal yearHigh
    BigDecimal priceAvg50
    BigDecimal priceAvg200
    Long volume
    Long avgVolume   
}
