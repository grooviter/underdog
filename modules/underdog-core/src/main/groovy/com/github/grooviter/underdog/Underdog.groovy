package com.github.grooviter.underdog

import com.github.grooviter.underdog.impl.TablesawDataFrame
import groovy.transform.NamedParam
import groovy.transform.NamedVariant
import tech.tablesaw.api.Table
import tech.tablesaw.io.csv.CsvReadOptions

import java.text.SimpleDateFormat
import java.time.format.DateTimeFormatterBuilder


class Underdog {
    @NamedVariant
    static DataFrame read_excel(
        String path,
        int skipRows,
        int skipFooter,
        List<String> nanValues,
        String dateFormat,
        String dateTimeFormat){
        return null
    }

    @NamedVariant
    static DataFrame read_csv(
            @NamedParam(required = true) String path,
            @NamedParam(required = false) String sep,
            @NamedParam(required = false) int skipRows,
            @NamedParam(required = false) int skipFooter,
            @NamedParam(required = false) List<String> nanValues,
            @NamedParam(required = false) String dateFormat,
            @NamedParam(required = false) String dateTimeFormat){
        CsvReadOptions.Builder builder = CsvReadOptions.builder(new File(path))

        if (dateFormat) {
            builder.dateFormat(dateFormat)
        }

        if (dateTimeFormat) {
            builder.dateTimeFormat(dateTimeFormat)
        }

        if (nanValues) {
            builder.missingValueIndicator(nanValues as String[])
        }

        if (sep) {
            builder.separator(sep.toCharacter())
        }

        Table table = Table.read().csv(builder.build())

        if (skipRows) {
            table = table.dropRange(skipRows)
        }

        if (skipFooter) {
            table = table.dropRange(table.rowCount() - skipFooter, table.rowCount())
        }

        return new TablesawDataFrame(table)
    }
}
