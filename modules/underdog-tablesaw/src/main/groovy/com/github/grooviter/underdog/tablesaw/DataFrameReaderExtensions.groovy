package com.github.grooviter.underdog.tablesaw

import tech.tablesaw.api.ColumnType
import tech.tablesaw.api.Table
import tech.tablesaw.io.DataFrameReader
import tech.tablesaw.io.csv.CsvReadOptions

import java.time.format.DateTimeFormatter

class DataFrameReaderExtensions {
    static Table csv(DataFrameReader _, Map params, String file) {
        CsvReadOptions.Builder options = CsvReadOptions.builder(file)

        if (params.sep) {
            assert checkSep(params.sep), "sep must be a single character representing csv separator"
            options.separator(params.sep.find() as char)
        }

        if (params.na) {
            assert checkNa(params.na), "na must be a list of potential NaN values"
            options.missingValueIndicator(params.na as String[])
        }

        if (params.dateTimeFormat) {
            assert checkDateTimeFormatters(params.dateTimeFormat), "dateTimeFormat must be a string or a DateTimeFormatter instance"
            options.dateTimeFormat(params.dateTimeFormat.toString())
        }

        if (params.dateFormat) {
            assert checkDateTimeFormatters(params.dateFormat), "dateFormat must be a string or a DateTimeFormatter instance"
            options.dateFormat(params.dateFormat.toString())
        }

        if (params.types) {
            assert checkTypes(params.types), "types are not type ColumnType"
            options.columnTypes(params.types as ColumnType[])
        }

        return Table.read().csv(options)
    }

    static boolean checkDateTimeFormatters(Object param) {
        return param instanceof String || param instanceof DateTimeFormatter
    }

    static boolean checkNa(Object param) {
        return param instanceof List && param.every { it instanceof String }
    }

    static boolean checkSep(Object param) {
        return param instanceof String && "$param".size() > 0
    }

    static boolean checkTypes(Object param) {
        return param instanceof List && param.every { it instanceof ColumnType }
    }
}
