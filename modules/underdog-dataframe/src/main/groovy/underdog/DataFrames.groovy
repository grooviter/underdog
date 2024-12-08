package underdog

import underdog.impl.TSCsvReader
import underdog.impl.TSCsvReaderOptions
import underdog.impl.TSDataFrame
import underdog.impl.TSExcelReader
import underdog.impl.TSExcelReaderOptions
import groovy.transform.NamedParam
import groovy.transform.NamedVariant
import tech.tablesaw.api.Table

import java.time.format.DateTimeFormatter

class DataFrames {

    DataFrame empty(String name = "") {
        return TSDataFrame.from(name)
    }

    DataFrame from(Map<String,?> from, String name) {
        return TSDataFrame.from(name, from)
    }

    DataFrame from(Collection<Map<String,?>> from, String name) {
        return TSDataFrame.from(name, from)
    }

    @NamedVariant
    DataFrame read_excel(
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

    @NamedVariant
    DataFrame read_csv(
            String path,
            @NamedParam(required = false) String sep = ',',
            @NamedParam(required = false) int skipRows = 0,
            @NamedParam(required = false) int skipFooter = 0,
            @NamedParam(required = false) List<String> nanValues = [],
            @NamedParam(required = false) boolean allowedDuplicatedNames = false,
            @NamedParam(required = false) String dateFormat = '',
            @NamedParam(required = false) String dateTimeFormat = '',
            @NamedParam(required = false) int maxCharsPerColumn = 500,
            @NamedParam(required = false) int maxNumberOfColumns = 50,
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
            builder.dateFormat(DateTimeFormatter.ofPattern(dateFormat))
        }

        if (allowedDuplicatedNames) {
            builder.allowDuplicateColumnNames(allowedDuplicatedNames)
        }

        if (dateTimeFormat) {
            builder.dateTimeFormat(DateTimeFormatter.ofPattern(dateTimeFormat))
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
