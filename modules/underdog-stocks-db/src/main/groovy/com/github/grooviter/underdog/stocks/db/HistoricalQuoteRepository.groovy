package com.github.grooviter.underdog.stocks.db

import com.github.grooviter.underdog.stocks.db.common.HistoricalRepository

import javax.sql.DataSource

class HistoricalQuoteRepository extends HistoricalRepository<HistoricalQuote, String> {
    HistoricalQuoteRepository(DataSource ds) {
        super(ds, HistoricalQuote)
    }
}
