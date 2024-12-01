package underdog.impl

//
// Source code recreated from a .class file by IntelliJ IDEA
// (powered by FernFlower decompiler)
//

import java.time.format.DateTimeFormatter
import java.util.function.Function;
import tech.tablesaw.api.ColumnType;
import tech.tablesaw.io.ReadOptions;
import tech.tablesaw.io.Source;

public class TSExcelReaderOptions extends ReadOptions {
    protected int sheetIndex;
    protected int skipRows
    protected int skipFooter

    protected TSExcelReaderOptions(Builder builder) {
        super(builder);
        this.sheetIndex = builder.sheetIndex;
        this.skipRows = builder.skipRows
        this.skipFooter = builder.skipFooter
    }

    public static Builder builder(Source source) {
        return new Builder(source);
    }

    public static Builder builder(File file) {
        return (new Builder(file)).tableName(file.getName());
    }

    public static Builder builder(String fileName) {
        return new Builder(new File(fileName));
    }

    public static Builder builder(URL url) throws IOException {
        return new Builder(url);
    }

    public static Builder builderFromFile(String fileName) {
        return new Builder(new File(fileName));
    }

    public static Builder builderFromUrl(String url) throws IOException {
        return new Builder(new URL(url));
    }

    public Integer sheetIndex() {
        return this.sheetIndex;
    }

    public static class Builder extends ReadOptions.Builder {
        protected int sheetIndex;
        protected int skipRows
        protected int skipFooter
        protected int startColumn
        protected int endColumn

        protected Builder(Source source) {
            super(source);
        }

        protected Builder(URL url) throws IOException {
            super(url);
        }

        public Builder(File file) {
            super(file);
        }

        public Builder(InputStream stream) {
            super(stream);
        }

        public Builder(Reader reader) {
            super(reader);
        }

        public TSExcelReaderOptions build() {
            return new TSExcelReaderOptions(this);
        }

        public Builder header(boolean header) {
            super.header(header);
            return this;
        }

        public Builder tableName(String tableName) {
            super.tableName(tableName);
            return this;
        }

        public Builder sample(boolean sample) {
            super.sample(sample);
            return this;
        }

        /** @deprecated */
        @Deprecated
        public Builder dateFormat(String dateFormat) {
            super.dateFormat(dateFormat);
            return this;
        }

        /** @deprecated */
        @Deprecated
        public Builder timeFormat(String timeFormat) {
            super.timeFormat(timeFormat);
            return this;
        }

        /** @deprecated */
        @Deprecated
        public Builder dateTimeFormat(String dateTimeFormat) {
            super.dateTimeFormat(dateTimeFormat);
            return this;
        }

        public Builder skipRows(int startRow) {
            this.skipRows = startRow
            return this
        }

        public Builder skipFooter(int endRow) {
            this.skipFooter = endRow
            return this
        }

        public Builder dateFormat(DateTimeFormatter dateFormat) {
            super.dateFormat(dateFormat);
            return this;
        }

        public Builder timeFormat(DateTimeFormatter timeFormat) {
            super.timeFormat(timeFormat);
            return this;
        }

        public Builder dateTimeFormat(DateTimeFormatter dateTimeFormat) {
            super.dateTimeFormat(dateTimeFormat);
            return this;
        }

        public Builder locale(Locale locale) {
            super.locale(locale);
            return this;
        }

        public Builder missingValueIndicator(String... missingValueIndicators) {
            super.missingValueIndicator(missingValueIndicators);
            return this;
        }

        public Builder minimizeColumnSizes() {
            super.minimizeColumnSizes();
            return this;
        }

        public Builder sheetIndex(int sheetIndex) {
            this.sheetIndex = sheetIndex;
            return this;
        }

        public Builder columnTypes(ColumnType[] columnTypes) {
            super.columnTypes(columnTypes);
            return this;
        }

        public Builder columnTypes(Function<String, ColumnType> columnTypeFunction) {
            super.columnTypes(columnTypeFunction);
            return this;
        }

        public Builder columnTypesPartial(Function<String, Optional<ColumnType>> columnTypeFunction) {
            super.columnTypesPartial(columnTypeFunction);
            return this;
        }

        public Builder columnTypesPartial(Map<String, ColumnType> columnTypeByName) {
            super.columnTypesPartial(columnTypeByName);
            return this;
        }
    }
}
