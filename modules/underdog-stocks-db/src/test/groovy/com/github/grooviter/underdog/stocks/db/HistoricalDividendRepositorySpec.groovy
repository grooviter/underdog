package com.github.grooviter.underdog.stocks.db

import com.github.grooviter.underdog.db.BaseSpec
import com.github.grooviter.underdog.db.Pagination
import org.testcontainers.spock.Testcontainers

import java.time.LocalDate

@Testcontainers
class HistoricalDividendRepositorySpec extends BaseSpec {

    def "check crud interface"() {
        when:
        def repository = new HistoricalDividendRepository(dataSource)
        def dividendEntry = new HistoricalDividend(symbol: "AMZN", date: LocalDate.now(), adjDividend: 12.34)
        repository.save(dividendEntry)

        and:
        def result = repository.list(new Pagination(0, 100))

        then:
        result.totalCount() == 1
        result.data().size() == 1
    }
}
