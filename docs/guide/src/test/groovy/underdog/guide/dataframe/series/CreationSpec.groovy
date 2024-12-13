package underdog.guide.dataframe.series

import spock.lang.Specification
import underdog.Series
import underdog.Underdog

class CreationSpec extends Specification {
    def "series from collection"() {
        setup:
        // --8<-- [start:series_from_collection]
        Series series = [1, 2, 3].toSeries("numbers")
        // --8<-- [end:series_from_collection]

        expect:
        series.size() == 3
    }

    def "series from transformation"() {
        setup:
        // --8<-- [start:series_as_transformation_sample]
        def numbers = Underdog.df().from([numbers: 1..10], "numbers")
        // --8<-- [end:series_as_transformation_sample]
        println numbers

        when:
        // --8<-- [start:series_as_transformation_creation]
        numbers['by_two'] = numbers['numbers'] * 2
        // --8<-- [end:series_as_transformation_creation]
        println numbers

        and:
        // --8<-- [start:series_from_value_creation]
        numbers['one'] = 1
        // --8<-- [end:series_from_value_creation]
        println numbers

        then:
        numbers.columns == ['numbers', 'by_two', 'one']
        numbers['by_two'].toList() == (1..10).collect { it * 2d }
        numbers['one'].toList() == (1..10).collect { 1 }
    }
}
