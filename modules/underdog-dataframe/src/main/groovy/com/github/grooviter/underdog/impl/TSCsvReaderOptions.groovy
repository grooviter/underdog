package com.github.grooviter.underdog.impl

import java.time.format.DateTimeFormatter;

import java.util.function.Function;
import tech.tablesaw.api.ColumnType;
import tech.tablesaw.io.ReadOptions;
import tech.tablesaw.io.Source;

class TSCsvReaderOptions extends ReadOptions {
    private final Character separator;
    private final Character quoteChar;
    private final Character escapeChar;
    private final String lineEnding;
    private final Integer maxNumberOfColumns;
    private final Character commentPrefix;
    private final boolean lineSeparatorDetectionEnabled;
    private final int sampleSize;
    private final int skipRows;

    private TSCsvReaderOptions(TSCsvReaderOptions.Builder builder) {
        super(builder);
        separator = builder.separator;
        quoteChar = builder.quoteChar;
        escapeChar = builder.escapeChar;
        lineEnding = builder.lineEnding;
        skipRows = builder.skipRows
        maxNumberOfColumns = builder.maxNumberOfColumns;
        commentPrefix = builder.commentPrefix;
        lineSeparatorDetectionEnabled = builder.lineSeparatorDetectionEnabled;
        sampleSize = builder.sampleSize;
    }

    @Override
    boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        TSCsvReaderOptions that = (TSCsvReaderOptions) o;
        return lineSeparatorDetectionEnabled == that.lineSeparatorDetectionEnabled
                && sampleSize == that.sampleSize
                && Objects.equals(separator, that.separator)
                && Objects.equals(quoteChar, that.quoteChar)
                && Objects.equals(escapeChar, that.escapeChar)
                && Objects.equals(lineEnding, that.lineEnding)
                && Objects.equals(skipRows, that.skipRows)
                && Objects.equals(maxNumberOfColumns, that.maxNumberOfColumns)
                && Objects.equals(commentPrefix, that.commentPrefix);
    }

    @Override
    int hashCode() {
        return Objects.hash(
                separator,
                quoteChar,
                escapeChar,
                lineEnding,
                skipRows,
                maxNumberOfColumns,
                commentPrefix,
                lineSeparatorDetectionEnabled,
                sampleSize);
    }

    static Builder builder(Source source) {
        return new Builder(source);
    }

    static Builder builder(File file) {
        return new Builder(file).tableName(file.getName());
    }

    static Builder builder(String fileName) {
        return new Builder(new File(fileName));
    }

    static Builder builder(URL url) throws IOException {
        return new Builder(url);
    }

    static Builder builderFromFile(String fileName) {
        return new Builder(new File(fileName));
    }

    static Builder builderFromString(String contents) {
        return new Builder(new StringReader(contents));
    }

    static Builder builderFromUrl(String url) throws IOException {
        return new Builder(new URL(url));
    }

    /**
     * This method may cause tablesaw to buffer the entire InputStream.
     *
     * <p>If you have a large amount of data, you can do one of the following: 1. Use the method
     * taking a File instead of a stream, or 2. Provide the array of column types as an option. If you
     * provide the columnType array, we skip type detection and can avoid reading the entire file
     */
    static Builder builder(InputStream stream) {
        return new Builder(stream);
    }

    /**
     * This method may cause tablesaw to buffer the entire InputStream.
     *
     * <p>If you have a large amount of data, you can do one of the following: 1. Use the method
     * taking a File instead of a reader, or 2. Provide the array of column types as an option. If you
     * provide the columnType array, we skip type detection and can avoid reading the entire file
     */
    static Builder builder(Reader reader) {
        return new Builder(reader);
    }

    /**
     * This method may cause tablesaw to buffer the entire InputStream.
     *
     * <p>If you have a large amount of data, you can do one of the following: 1. Use the method
     * taking a File instead of a reader, or 2. Provide the array of column types as an option. If you
     * provide the columnType array, we skip type detection and can avoid reading the entire file
     */
    static Builder builder(InputStreamReader reader) {
        return new Builder(reader);
    }

    ColumnType[] columnTypes() {
        return columnTypeReadOptions.columnTypes();
    }

    Character separator() {
        return separator;
    }

    Character quoteChar() {
        return quoteChar;
    }

    Character escapeChar() {
        return escapeChar;
    }

    String lineEnding() {
        return lineEnding;
    }

    int skipRows() {
        return skipRows
    }

    boolean lineSeparatorDetectionEnabled() {
        return lineSeparatorDetectionEnabled;
    }

    Integer maxNumberOfColumns() {
        return maxNumberOfColumns;
    }

    Character commentPrefix() {
        return commentPrefix;
    }

    int maxCharsPerColumn() {
        return maxCharsPerColumn;
    }

    int sampleSize() {
        return sampleSize;
    }

    static class Builder extends ReadOptions.Builder {

        private Character separator;
        private Character quoteChar;
        private Character escapeChar;
        private String lineEnding;
        private int skipRows;
        private Integer maxNumberOfColumns = 10_000;
        private Character commentPrefix;
        private boolean lineSeparatorDetectionEnabled = true;
        private int sampleSize = -1;

        protected Builder(Source source) {
            super(source);
        }

        protected Builder(URL url) throws IOException {
            super(url);
        }

        protected Builder(File file) {
            super(file);
        }

        protected Builder(InputStreamReader reader) {
            super(reader);
        }

        protected Builder(Reader reader) {
            super(reader);
        }

        protected Builder(InputStream stream) {
            super(stream);
        }

        @Override
        Builder columnTypes(ColumnType[] columnTypes) {
            super.columnTypes(columnTypes);
            return this;
        }

        @Override
        Builder columnTypes(Function<String, ColumnType> columnTypeFunction) {
            super.columnTypes(columnTypeFunction);
            return this;
        }

        @Override
        Builder columnTypesPartial(Function<String, Optional<ColumnType>> columnTypeFunction) {
            super.columnTypesPartial(columnTypeFunction);
            return this;
        }

        @Override
        Builder columnTypesPartial(Map<String, ColumnType> columnTypeByName) {
            super.columnTypesPartial(columnTypeByName);
            return this;
        }

        Builder separator(Character separator) {
            this.separator = separator;
            return this;
        }

        Builder quoteChar(Character quoteChar) {
            this.quoteChar = quoteChar;
            return this;
        }

        Builder escapeChar(Character escapeChar) {
            this.escapeChar = escapeChar;
            return this;
        }

        Builder lineEnding(String lineEnding) {
            this.lineEnding = lineEnding;
            this.lineSeparatorDetectionEnabled = false;
            return this;
        }

        Builder skipRows(int skipRows){
            this.skipRows = skipRows
            return this
        }

        /**
         * Defines maximal value of columns in csv file.
         *
         * @param maxNumberOfColumns - must be positive integer. Default is 10,000
         */
        Builder maxNumberOfColumns(Integer maxNumberOfColumns) {
            this.maxNumberOfColumns = maxNumberOfColumns;
            return this;
        }

        Builder commentPrefix(Character commentPrefix) {
            this.commentPrefix = commentPrefix;
            return this;
        }

        /**
         * Defines the maximum number of rows to be read from the file. Sampling is performed in a
         * single pass using the reservoir sampling algorithm
         * (https://en.wikipedia.org/wiki/Reservoir_sampling). Given a file with 'n' rows, if
         * 'numSamples is smaller than 'n', than exactly 'numSamples' random samples are returned; if
         * 'numSamples' is greater than 'n', then only 'n' samples are returned (no oversampling is
         * performed to increase the data to match 'numSamples').
         */
        Builder sampleSize(int numSamples) {
            this.sampleSize = numSamples;
            return this;
        }

        @Override
        TSCsvReaderOptions build() {
            return new TSCsvReaderOptions(this);
        }

        // Override super-class setters to return an instance of this class

        @Override
        Builder header(boolean header = false) {
            super.header(header);
            return this;
        }

        /**
         * Enable reading of a table with duplicate column names. After the first appearance of a column
         * name, subsequent appearances will have a number appended.
         *
         * @param allow if true, duplicate names will be allowed
         */
        @Override
        Builder allowDuplicateColumnNames(Boolean allow) {
            super.allowDuplicateColumnNames(allow);
            return this;
        }

        @Override
        Builder columnTypesToDetect(List<ColumnType> columnTypesToDetect) {
            super.columnTypesToDetect(columnTypesToDetect);
            return this;
        }

        @Override
        Builder tableName(String tableName) {
            super.tableName(tableName);
            return this;
        }

        @Override
        Builder sample(boolean sample) {
            super.sample(sample);
            return this;
        }

        @Override
        Builder dateFormat(DateTimeFormatter dateFormat) {
            super.dateFormat(dateFormat);
            return this;
        }

        @Override
        Builder timeFormat(DateTimeFormatter timeFormat) {
            super.timeFormat(timeFormat);
            return this;
        }

        @Override
        Builder dateTimeFormat(DateTimeFormatter dateTimeFormat) {
            super.dateTimeFormat(dateTimeFormat);
            return this;
        }

        @Override
        Builder maxCharsPerColumn(int maxCharsPerColumn) {
            super.maxCharsPerColumn(maxCharsPerColumn);
            return this;
        }

        @Override
        Builder locale(Locale locale) {
            super.locale(locale);
            return this;
        }

        @Override
        Builder missingValueIndicator(String... missingValueIndicators) {
            super.missingValueIndicator(missingValueIndicators);
            return this;
        }

        @Override
        Builder minimizeColumnSizes() {
            super.minimizeColumnSizes();
            return this;
        }

        @Override
        Builder ignoreZeroDecimal(boolean ignoreZeroDecimal) {
            super.ignoreZeroDecimal(ignoreZeroDecimal);
            return this;
        }

        @Override
        Builder skipRowsWithInvalidColumnCount(boolean skipRowsWithInvalidColumnCount) {
            super.skipRowsWithInvalidColumnCount(skipRowsWithInvalidColumnCount);
            return this;
        }
    }
}
