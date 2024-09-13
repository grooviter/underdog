package com.github.grooviter.underdog.tablesaw.ast

import com.github.grooviter.underdog.tablesaw.test.BaseSpec
import tech.tablesaw.api.DoubleColumn
import tech.tablesaw.api.Table
import tech.tablesaw.columns.Column
import tech.tablesaw.selection.Selection

import java.time.LocalDate
import java.time.LocalDateTime
import java.time.Month

class IndexBasedQueriesSpec extends BaseSpec {

    void 'query column chaining method calls vs index based calls'() {
        given: "a verbose form of using selection"
        Column sugarCol = foodTable['SUGAR', DoubleColumn]
        Selection query = sugarCol.isGreaterThan(3.5) & sugarCol.isLessThanOrEqualTo(3.55)
        Column idsVerbose = foodTable.where(query).column("ID")

        when: "using index based syntax"
        Column idsIndexBased = foodTable[foodTable['SUGAR'] > 3.5 & foodTable["SUGAR"] <= 3.55, "ID"]

        then: "both queries should have the same result"
        idsIndexBased.size() == idsVerbose.size()
    }

    void 'query column combining equals with lessThanOrEqualTo'() {
        when:
        Column ids = foodTable[foodTable['BRAND ID'] == 214 & foodTable['SUGAR'] <= 1, "ID"]

        then:
        ids.size() == 8
    }

    void 'query column using equals'() {
        when:
        Column ids = foodTable[foodTable['BRAND ID'] == 214, "ID"]

        then:
        ids.size() == 37

        when:
        ids = foodTable[foodTable['SUBGROUP NAME'] == "Salsas", "ID"]

        then:
        ids.size() == 84
    }

    void 'query column using greater and less than with dates'() {
        given:
        LocalDateTime from = LocalDateTime.of(2016, 01, 01, 00, 00)

        when: "using a reference"
        Column ids = ratesTable[ratesTable["time"] > from, "m3"]

        then: "it should work and compare with the proper type"
        ids[0] == 0.22

        when: "using the full method call expression"
        ids = ratesTable[ratesTable["time"] > LocalDateTime.of(2016, 01, 01, 00, 00), "m3"]

        then: "it should resolve the proper column type too"
        ids[0] == 0.22
    }

    void 'query table with date selection'() {
        given:
        LocalDate from = LocalDate.of(2016, Month.JANUARY, 1)

        when:
        Table table = ratesTable[ratesTable['time'] > from, ['y1', 'y10']]

        then:
        table.size() == 1
        table.columnCount() == 2
    }

    void 'query table with string regex'() {
        when:
        Table table = foodTable[foodTable['NAME'] ==~ /.*sin miel.*/, ['NAME', 'SUGAR', 'CARBS']]

        then:
        table.size() == 7
    }
}
