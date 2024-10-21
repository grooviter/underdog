package com.github.grooviter.underdog.stocks.db

import com.github.grooviter.underdog.stocks.db.common.HistoricalRepository

import javax.sql.DataSource

class HistoricalSplitRepository extends HistoricalRepository<HistoricalSplit, String> {
    HistoricalSplitRepository(DataSource ds) {
        super(ds, HistoricalSplit)
    }
}
