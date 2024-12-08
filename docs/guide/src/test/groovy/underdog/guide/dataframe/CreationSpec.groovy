package underdog.guide.dataframe

import spock.lang.Specification
import underdog.DataFrame
import underdog.Series
import underdog.Underdog

class CreationSpec extends Specification {

    def "Underdog.df().empty()"() {
        setup:
        // tag::empty[]
        DataFrame emptyDataFrame = Underdog.df().empty()
        // end::empty[]
        expect:
        emptyDataFrame.size() == 0
    }

    def "Underdog.df().from(name, map)"() {
        setup:
        // tag::from_map[]
        // creating a map
        def map = [
            names: ["John", "Laura", "Ursula"],
            ages: [22, 34, 83]
        ]

        // creating a dataframe from the map
        DataFrame map2DataFrame = Underdog.df().from(map, "people-dataframe")
        // end::from_map[]
        println map2DataFrame
        expect:
        map2DataFrame.size() == 3
    }

    def "map.toDataFrame(name)"() {
        setup:
        // tag::map_extension[]
        // creating a map
        def map = [
            names: ["John", "Laura", "Ursula"],
            ages: [22, 34, 83]
        ]

        // creating a dataframe from a map
        DataFrame map2DataFrame = map.toDataFrame("people-dataframe")
        // end::map_extension[]
        expect:
        map2DataFrame.size() == 3
    }

    def "Underdog.df().from(colOfMaps, name)"() {
        setup:
        // tag::collectionsOfMaps[]
        // creating a list of maps
        def list = [
            [name: "John", age: 22],
            [name: "Laura", age: 34],
            [name: "Ursula", age: 83]
        ]

        // creating a dataframe from the list
        DataFrame colOfMaps2DataFrame = Underdog.df().from(list, "people-dataframe")
        // end::collectionsOfMaps[]
        println colOfMaps2DataFrame
        expect:
        colOfMaps2DataFrame.columns == ['name', 'age']
        colOfMaps2DataFrame.size() == 3
    }

    def "colOfMaps.toDataFrame()"() {
        setup:
        // tag::collectionsOfMaps_extension[]
        // creating a list of maps
        def list = [
            [name: "John", age: 22],
            [name: "Laura", age: 34],
            [name: "Ursula", age: 83]
        ]

        // creating a dataframe from the list
        DataFrame colOfMaps2DataFrame = list.toDataFrame("people-dataframe")
        // end::collectionsOfMaps_extension[]
        println colOfMaps2DataFrame

        expect:
        colOfMaps2DataFrame.columns == ['name', 'age']
        colOfMaps2DataFrame.size() == 3
    }

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
