package com.github.grooviter.underdog.stocks.db

import com.github.grooviter.underdog.db.AbstractRepository

import javax.sql.DataSource

class StockRepository extends AbstractRepository<Stock, String> {
    StockRepository(DataSource ds) {
        super(ds, Stock)
    }
}
