package underdog.guide.dataframe.dataframe

import spock.lang.Specification
import underdog.DataFrame
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
}
