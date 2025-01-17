package underdog.impl

import com.google.common.io.CharStreams;
import com.univocity.parsers.common.AbstractParser;
import com.univocity.parsers.csv.CsvFormat;
import com.univocity.parsers.csv.CsvParser;
import com.univocity.parsers.csv.CsvParserSettings;
import javax.annotation.concurrent.Immutable;
import org.apache.commons.math3.util.Pair;
import tech.tablesaw.api.ColumnType;
import tech.tablesaw.api.Table;
import tech.tablesaw.io.*;

@Immutable
public class TSCsvReader extends FileReader implements DataReader<TSCsvReaderOptions> {

    private static final TSCsvReader INSTANCE = new TSCsvReader();

    static {
        register(Table.defaultReaderRegistry);
    }

    public static void register(ReaderRegistry registry) {
        registry.registerExtension("csv", INSTANCE);
        registry.registerMimeType("text/csv", INSTANCE);
        registry.registerOptions(TSCsvReaderOptions.class, INSTANCE);
    }

    /** Constructs a TSCsvReader */
    public TSCsvReader() {
        super();
    }

    /**
     * Determines column types if not provided by the user Reads all input into memory unless File was
     * provided
     */
    private Pair<Reader, ReadOptions.ColumnTypeReadOptions> getReaderAndColumnTypes(
            Source source, TSCsvReaderOptions options) throws IOException {
        ReadOptions.ColumnTypeReadOptions columnTypeReadOptions = options.columnTypeReadOptions();
        byte[] bytesCache = null;

        boolean need2ParseFile =
                !columnTypeReadOptions.hasColumnTypeForAllColumns()
                        && (!options.header()
                        || !columnTypeReadOptions.hasColumnTypeForAllColumnsIfHavingColumnNames());
        if (need2ParseFile) {
            Reader reader = source.createReader(null);
            if (source.file() == null) {
                String s = CharStreams.toString(reader);
                bytesCache = source.getCharset() != null ? s.getBytes(source.getCharset()) : s.getBytes();
                // create a new reader since we just exhausted the existing one
                reader = source.createReader(bytesCache);
            }
            ColumnType[] detectedColumnTypes = detectColumnTypes(reader, options)

            // If no columns where returned from detectColumnTypes leave initial options (that's the case
            // for only header present)
            if (detectedColumnTypes.length > 0) {
                columnTypeReadOptions = ReadOptions.ColumnTypeReadOptions.of(detectedColumnTypes);
            }
        }

        return Pair.create(source.createReader(bytesCache), columnTypeReadOptions);
    }

    Table read(TSCsvReaderOptions options) {
        try {
            return read(options, false);
        } catch (IOException e) {
            throw new RuntimeIOException(e);
        }
    }

    protected Table read(TSCsvReaderOptions options, boolean headerOnly) throws IOException {
        Pair<Reader, ReadOptions.ColumnTypeReadOptions> pair =
                getReaderAndColumnTypes(options.source(), options);
        Reader reader = pair.getKey();
        ReadOptions.ColumnTypeReadOptions columnTypeReadOptions = pair.getValue();

        AbstractParser<?> parser = csvParser(options);

        try {
            return parseRows(
                    options, headerOnly, reader, columnTypeReadOptions, parser, options.sampleSize());
        } finally {
            if (options.source().reader() == null) {
                // if we get a reader back from options it means the client opened it, so let the client
                // close it
                // if it's null, we close it here.
                parser.stopParsing();
                reader.close();
            }
        }
    }

    /**
     * Returns a string representation of the column types in file {@code csvFilename}, as determined
     * by the type-detection algorithm
     *
     * <p>This method is intended to help analysts quickly fix any erroneous types, by printing out
     * the types in a format such that they can be edited to correct any mistakes, and used in an
     * array literal
     *
     * <p>For example:
     *
     * <p>LOCAL_DATE, // 0 date SHORT, // 1 approval STRING, // 2 who
     *
     * <p>Note that the types are array separated, and that the index position and the column name are
     * printed such that they would be interpreted as comments if you paste the output into an array:
     *
     * <p>
     *
     * @throws IOException if file cannot be read
     */
    public String printColumnTypes(TSCsvReaderOptions options) throws IOException {
        Table structure = read(options, true).structure();
        return getTypeString(structure);
    }

    /**
     * Estimates and returns the type for each column in the delimited text file {@code file}
     *
     * <p>The type is determined by checking a sample of the data in the file. Because only a sample
     * of the data is checked, the types may be incorrect. If that is the case a Parse Exception will
     * be thrown.
     *
     * <p>The method {@code printColumnTypes()} can be used to print a list of the detected columns
     * that can be corrected and used to explicitly specify the correct column types.
     */
    protected ColumnType[] detectColumnTypes(Reader reader, TSCsvReaderOptions options) {
        boolean header = options.header();
        CsvParser parser = csvParser(options);

        try {
            String[] columnNames = null;
            if (header) {
                parser.beginParsing(reader);
                columnNames = getColumnNames(options, options.columnTypeReadOptions(), parser);
            }
            return getColumnTypes(reader, options, 0, parser, columnNames);
        } finally {
            parser.stopParsing();
            // we don't close the reader since we didn't create it
        }
    }

    private CsvParser csvParser(TSCsvReaderOptions options) {
        CsvParserSettings settings = new CsvParserSettings();
        settings.setLineSeparatorDetectionEnabled(options.lineSeparatorDetectionEnabled());
        settings.setFormat(csvFormat(options));
        settings.setNumberOfRowsToSkip(options.skipRows())
        settings.setMaxCharsPerColumn(options.maxCharsPerColumn());
        if (options.maxNumberOfColumns() != null) {
            settings.setMaxColumns(options.maxNumberOfColumns());
        }
        return new CsvParser(settings);
    }

    private CsvFormat csvFormat(TSCsvReaderOptions options) {
        CsvFormat format = new CsvFormat();
        if (options.quoteChar() != null) {
            format.setQuote(options.quoteChar());
        }
        if (options.escapeChar() != null) {
            format.setQuoteEscape(options.escapeChar());
        }
        if (options.separator() != null) {
            format.setDelimiter(options.separator());
        }
        if (options.lineEnding() != null) {
            format.setLineSeparator(options.lineEnding());
        }
        if (options.commentPrefix() != null) {
            format.setComment(options.commentPrefix());
        }
        return format;
    }

    @Override
    public Table read(Source source) {
        return read(TSCsvReaderOptions.builder(source).build());
    }
}
