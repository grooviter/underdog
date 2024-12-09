package underdog.guide.dataframe.dataframe

import com.univocity.parsers.common.TextParsingException
import spock.lang.Specification
import underdog.DataFrame
import underdog.Underdog

class CsvImportExportSpec extends Specification {
    def "import csv"() {
        setup:
        // tag::simple_read_csv[]
        DataFrame dataframe = Underdog.df().read_csv("src/test/resources/data/tornadoes_1950-2014.csv")
        // end::simple_read_csv[]
        println(dataframe)

        expect:
        dataframe.size() == 59945
    }

    def "import csv: custom separator"() {
        setup:
        def filePath = "src/test/resources/data/dataframe/io_custom_separator.csv"
        // tag::custom_separator[]
        def dataframe = Underdog.df().read_csv(filePath, sep: ";")
        // end::custom_separator[]

        expect:
        dataframe.columns == ['name', 'age']
        dataframe.size() == 3
    }

    def "import csv: allow repeated cols"() {
        setup:
        def filePath = "src/test/resources/data/dataframe/io_repeated_cols.csv"
        // tag::allow_repeated_cols[]
        def dataframe = Underdog.df().read_csv(filePath, allowedDuplicatedNames: true)
        // end::allow_repeated_cols[]
        println(dataframe)
        expect:
        dataframe.columns.size() == 8
        dataframe.size() == 1
    }

    def "import csv: custom missing data"() {
        setup:
        def filePath = "src/test/resources/data/dataframe/io_custom_missing_data.csv"
        // tag::custom_missing_data[]
        def dataframe = Underdog.df().read_csv(filePath, nanValues: ['NONE', 'N/C'])
        // end::custom_missing_data[]
        println(dataframe)

        expect:
        dataframe.columns.size() == 3
        dataframe.size() == 4
    }

    def "import csv: custom date format"() {
        setup:
        def filePath = "src/test/resources/data/dataframe/io_custom_date_format.csv"
        // tag::custom_date_format[]
        def dataframe = Underdog.df().read_csv(filePath, dateFormat: "yyyy-MM-dd HH:mm:ss+00:00")
        // end::custom_date_format[]
        println(dataframe)

        expect:
        dataframe.size() == 3
    }

    def "import csv: skipping rows"() {
        setup:
        def filePath = "src/test/resources/data/dataframe/io_skipping_rows.csv"
        // tag::skipping_rows[]
        def dataframe = Underdog.df()
            .read_csv(filePath,
                header: false, // <1>
                skipRows: 8, // <2>
                skipFooter: 4 // <3>
            ).renameSeries(columns: ['city', 'id']) // <4>
        // end::skipping_rows[]
        println(dataframe)
        expect:
        dataframe.size() == 2
    }

    def "import csv: limiting col chars"() {
        when:
        def filePath = "src/test/resources/data/dataframe/io_limiting_chars.csv"
        // tag::col_char_limit[]
        def dataframe = Underdog.df().read_csv(filePath, maxCharsPerColumn: 20)
        // end::col_char_limit[]

        then:
        thrown(TextParsingException)
    }

    def "import csv: limiting number of cols"() {
        when:
        def filePath = "src/test/resources/data/dataframe/io_limiting_cols.csv"
        // tag::col_limit[]
        def dataframe = Underdog.df().read_csv(filePath, maxNumberOfColumns: 2)
        // end::col_limit[]
        then:
        thrown(TextParsingException)
    }
}
