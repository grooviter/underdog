package underdog.guide.dataframe.series

import spock.lang.Specification
import underdog.Series
import underdog.Underdog

class CreationSpec extends Specification {
    def "series from collection"() {
        setup:
        // tag::series_from_collection[]
        Series series = [1, 2, 3].toSeries("numbers")
        // end::series_from_collection[]

        expect:
        series.size() == 3
    }

    def "series from transformation"() {
        setup:
        // tag::series_as_transformation_sample[]
        def numbers = Underdog.df().from([numbers: 1..10], "numbers")
        // end::series_as_transformation_sample[]
        println numbers

        when:
        // tag::series_as_transformation_creation[]
        numbers['by_two'] = numbers['numbers'] * 2
        // end::series_as_transformation_creation[]
        println numbers

        and:
        // tag::series_from_value_creation[]
        numbers['one'] = 1
        // end::series_from_value_creation[]
        println numbers

        then:
        numbers.columns == ['numbers', 'by_two', 'one']
        numbers['by_two'].toList() == (1..10).collect { it * 2d }
        numbers['one'].toList() == (1..10).collect { 1 }
    }
}
