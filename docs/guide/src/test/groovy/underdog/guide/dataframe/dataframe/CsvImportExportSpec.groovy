package underdog.guide.dataframe.dataframe

import com.univocity.parsers.common.TextParsingException
import spock.lang.Specification
import underdog.DataFrame
import underdog.Underdog

class CsvImportExportSpec extends Specification {
    def "import csv"() {
        setup:
        // --8<-- [start:simple_read_csv]
        DataFrame dataframe = Underdog.df().read_csv("src/test/resources/data/tornadoes_1950-2014.csv")
        // --8<-- [end:simple_read_csv]
        println(dataframe)

        expect:
        dataframe.size() == 59945
    }

    def "import csv: custom separator"() {
        setup:
        def filePath = "src/test/resources/data/dataframe/io_custom_separator.csv"
        // --8<-- [start:custom_separator]
        def dataframe = Underdog.df().read_csv(filePath, sep: ";")
        // --8<-- [end:custom_separator]

        expect:
        dataframe.columns == ['name', 'age']
        dataframe.size() == 3
    }

    def "import csv: allow repeated cols"() {
        setup:
        def filePath = "src/test/resources/data/dataframe/io_repeated_cols.csv"
        // --8<-- [start:allow_repeated_cols]
        def dataframe = Underdog.df().read_csv(filePath, allowedDuplicatedNames: true)
        // --8<-- [end:allow_repeated_cols]
        println(dataframe)
        expect:
        dataframe.columns.size() == 8
        dataframe.size() == 1
    }

    def "import csv: custom missing data"() {
        setup:
        def filePath = "src/test/resources/data/dataframe/io_custom_missing_data.csv"
        // --8<-- [start:custom_missing_data]
        def dataframe = Underdog.df().read_csv(filePath, nanValues: ['NONE', 'N/C'])
        // --8<-- [end:custom_missing_data]
        println(dataframe)

        expect:
        dataframe.columns.size() == 3
        dataframe.size() == 4
    }

    def "import csv: custom date format"() {
        setup:
        def filePath = "src/test/resources/data/dataframe/io_custom_date_format.csv"
        // --8<-- [start:custom_date_format]
        def dataframe = Underdog.df().read_csv(filePath, dateFormat: "yyyy-MM-dd HH:mm:ss+00:00")
        // --8<-- [end:custom_date_format]
        println(dataframe)

        expect:
        dataframe.size() == 3
    }

    def "import csv: skipping rows"() {
        setup:
        def filePath = "src/test/resources/data/dataframe/io_skipping_rows.csv"
        // --8<-- [start:skipping_rows]
        def dataframe = Underdog.df()
            .read_csv(filePath,
                header: false,    // not using first row as header
                skipRows: 8,      // skipping rows at the beginning of the file
                skipFooter: 4     // skipping rows at the end of the file
            ).renameSeries(columns: ['city', 'id']) // renaming series names with the list passed as parameter
        // --8<-- [end:skipping_rows]
        println(dataframe)
        expect:
        dataframe.size() == 2
    }

    def "import csv: limiting col chars"() {
        when:
        def filePath = "src/test/resources/data/dataframe/io_limiting_chars.csv"
        // --8<-- [start:col_char_limit]
        def dataframe = Underdog.df().read_csv(filePath, maxCharsPerColumn: 20)
        // --8<-- [end:col_char_limit]

        then:
        thrown(TextParsingException)
    }

    def "import csv: limiting number of cols"() {
        when:
        def filePath = "src/test/resources/data/dataframe/io_limiting_cols.csv"
        // --8<-- [start:col_limit]
        def dataframe = Underdog.df().read_csv(filePath, maxNumberOfColumns: 2)
        // --8<-- [end:col_limit]
        then:
        thrown(TextParsingException)
    }
}
