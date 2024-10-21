package com.github.grooviter.underdog.stocks.db

import com.github.grooviter.underdog.stocks.db.common.HistoricalRepository

import javax.sql.DataSource

class HistoricalDividendRepository extends HistoricalRepository<HistoricalDividend, String> {
    HistoricalDividendRepository(DataSource ds) {
        super(ds, HistoricalDividend)
    }
}
