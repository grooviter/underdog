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
        @NamedParam(required = false) boolean headerInFirstRow,
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

        if (headerInFirstRow) {
            options.header(headerInFirstRow)
        }

        return new TSDataFrame(new TSExcelReader().read(options.build()))
    }

    static DataFrame read_csv(String path) {
        return read_csv([:], path)
    }

    @NamedVariant
    static DataFrame read_csv(
            String path,
            @NamedParam(required = false) String sep,
            @NamedParam(required = false) int skipRows,
            @NamedParam(required = false) int skipFooter,
            @NamedParam(required = false) List<String> nanValues,
            @NamedParam(required = false) boolean allowedDuplicatedNames,
            @NamedParam(required = false) String dateFormat,
            @NamedParam(required = false) String dateTimeFormat,
            @NamedParam(required = false) int maxCharsPerColumn,
            @NamedParam(required = false) int maxNumberOfColumns,
            @NamedParam(required = false) boolean header = true){
        TSCsvReaderOptions.Builder builder = TSCsvReaderOptions.builder(new File(path))

        builder.header(header)

        if (maxCharsPerColumn) {
            builder.maxCharsPerColumn(maxCharsPerColumn)
        }

        if (maxNumberOfColumns) {
            builder.maxNumberOfColumns(maxNumberOfColumns)
        }

        if (dateFormat) {
            builder.dateFormat(dateFormat)
        }

        if (allowedDuplicatedNames) {
            builder.allowDuplicateColumnNames(allowedDuplicatedNames)
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
