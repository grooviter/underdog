package com.github.grooviter.underdog.tablesaw.test

import spock.lang.Shared
import spock.lang.Specification
import tech.tablesaw.api.ColumnType
import tech.tablesaw.api.Table
import tech.tablesaw.io.csv.CsvReadOptions

class BaseSpec extends Specification {
    static final String FOOD_CSV_PATH = "src/test/resources/data/food.csv"
    static final String RATES_CSV_PATH = "src/test/resources/data/interest-rates.csv"

    @Shared Table foodTable
    @Shared Table ratesTable

    void setup() {
        def foodOptions = CsvReadOptions.builder(FOOD_CSV_PATH)
                .separator(';' as char)
                .header(true)
                .missingValueIndicator('NaN')

        foodTable = Table.read().csv(foodOptions)

        def ratesOptions = CsvReadOptions.builder(RATES_CSV_PATH)
                .separator(',' as char)
                .header(true)
        .dateTimeFormat("yyyy-MM-dd HH:mm:ss.SSS x")
                .columnTypes(
                    ColumnType.DOUBLE,
                    ColumnType.DOUBLE,
                    ColumnType.DOUBLE,
                    ColumnType.DOUBLE,
                    ColumnType.DOUBLE,
                    ColumnType.DOUBLE,
                    ColumnType.DOUBLE,
                    ColumnType.LOCAL_DATE_TIME,
                    ColumnType.DOUBLE,
                    ColumnType.DOUBLE,
                    ColumnType.DOUBLE)
                .missingValueIndicator('NaN')

        ratesTable = Table.read().csv(ratesOptions)
    }
}
