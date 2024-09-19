package com.github.grooviter.underdog

import com.github.grooviter.underdog.impl.TSCsvReader
import com.github.grooviter.underdog.impl.TSCsvReaderOptions
import com.github.grooviter.underdog.impl.TSDataFrame
import com.github.grooviter.underdog.impl.TSExcelReader
import com.github.grooviter.underdog.impl.TSExcelReaderOptions
import groovy.transform.NamedParam
import groovy.transform.NamedVariant
import tech.tablesaw.api.Table
import tech.tablesaw.io.csv.CsvReadOptions
import tech.tablesaw.io.xlsx.XlsxReadOptions
import tech.tablesaw.io.xlsx.XlsxReader

class Underdog {
    @NamedVariant
    static DataFrame read_excel(
        @NamedParam(required = true) String path,
        @NamedParam(required = false) int skipRows,
        @NamedParam(required = false) int skipFooter,
        @NamedParam(required = false) Object sheet_name,
        @NamedParam(required = false) List<String> nanValues,
        @NamedParam(required = false) String dateFormat,
        @NamedParam(required = false) String dateTimeFormat){
        def options = TSExcelReaderOptions.builder(path)

        if (sheet_name) {
            options.sheetIndex(sheet_name.toString().toInteger())
        }

        if (skipRows){
            options.skipRows(skipRows)
        }

        if (skipFooter){
            options.skipFooter(skipFooter)
        }

        if (dateFormat){
            options.dateFormat(dateFormat)
        }

        if (dateTimeFormat) {
            options.dateTimeFormat(dateTimeFormat)
        }

        if (nanValues) {
            options.missingValueIndicator(nanValues as String[])
        }

        return new TSDataFrame(new TSExcelReader().read(options.build()))
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
        TSCsvReaderOptions.Builder builder = TSCsvReaderOptions.builder(new File(path))

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

        if (skipRows) {
            builder.skipRows(skipRows)
        }

        Table table = new TSCsvReader().read(builder.build())

        if (skipFooter) {
            table = table.dropRange(table.rowCount() - skipFooter, table.rowCount())
        }

        return new TSDataFrame(table)
    }
}
