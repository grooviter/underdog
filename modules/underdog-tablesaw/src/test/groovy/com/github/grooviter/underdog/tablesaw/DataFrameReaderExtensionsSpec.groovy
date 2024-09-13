package com.github.grooviter.underdog.tablesaw

import com.github.grooviter.underdog.tablesaw.test.BaseSpec
import tech.tablesaw.api.Table

import static tech.tablesaw.api.ColumnType.LOCAL_DATE_TIME
import static tech.tablesaw.api.ColumnType.SKIP

class DataFrameReaderExtensionsSpec extends BaseSpec {
    static final String FILE_NAME = 'src/test/resources/data/interest-rates.csv'

    void 'reading csv with filename and separator type'() {
        when:
        Table table = Table.read().csv(FILE_NAME, sep: ',')

        then:
        table.size() == 313
    }

    void 'reading csv with filename, sep, and, cols'() {
        when:
        Table interestRates = Table.read().csv(FILE_NAME,
            sep:            ',',
            na:             ['NaN'],
            dateTimeFormat: "yyyy-MM-dd HH:mm:ss.SSS x",
            types:          [SKIP] * 7 + [LOCAL_DATE_TIME] + [SKIP] * 3
        )

        then:
        interestRates.columnCount() == 1
    }
}
