package com.github.grooviter.underdog.stocks.db

import com.github.grooviter.underdog.db.BaseSpec
import com.github.grooviter.underdog.db.Pagination
import com.github.grooviter.underdog.db.migration.Migrator
import org.testcontainers.spock.Testcontainers

import java.time.LocalDate

@Testcontainers
class HistoricalDividendRepositorySpec extends BaseSpec {

    def "check crud interface"() {
        setup:
        def ds = dataSource
        new Migrator(ds).execute()

        when:
        def repository = new HistoricalDividendRepository(ds)
        def dividendEntry = new HistoricalDividend(symbol: "AMZN", date: LocalDate.now(), adjDividend: 12.34)
        repository.save(dividendEntry)

        and:
        def result = repository.list(new Pagination(0, 100))

        then:
        result.totalCount() == 1
        result.data().size() == 1
    }
}
