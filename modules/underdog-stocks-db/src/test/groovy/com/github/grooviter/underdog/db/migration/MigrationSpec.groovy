package com.github.grooviter.underdog.db.migration

import com.github.grooviter.underdog.db.BaseSpec
import com.github.grooviter.underdog.db.Pagination
import com.github.grooviter.underdog.stocks.db.Stock
import com.github.grooviter.underdog.stocks.db.StockRepository
import org.testcontainers.spock.Testcontainers

import javax.sql.DataSource

@Testcontainers
class MigrationSpec extends BaseSpec {
    def "Checking migration works"() {
        setup:
        DataSource ds = dataSource
        new Migrator(ds).execute()

        when:
        StockRepository repository = new StockRepository(ds)

        and:
        Stock stock = new Stock(symbol: "AMZN", name: "Amazon", stockExchange: "NASDAQ")
        Stock saved = repository.save(stock)

        and:
        def result = repository.list(new Pagination(0, 100))

        then:
        result.data().first().name == saved.name
    }
}
