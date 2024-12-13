package underdog.guide.graphs

import underdog.Underdog
import spock.lang.Specification

class OperatorsSpec extends Specification {
    def "merging graphs"() {
        when:
        // --8<-- [start:operators_plus]
        def names1 = Underdog.graphs().graph(String) {
            ["John", "Lisa", "Robert"].each(delegate::vertex)
        }

        def names2 = Underdog.graphs().graph(String) {
            ["Anna", "Vesper", "Tania"].each(delegate::vertex)
        }

        def names3 = names1 + names2
        // --8<-- [end:operators_plus]
        then:
        // --8<-- [start:operators_plus_result]
        assert names3.vertices == ["John", "Lisa", "Robert", "Anna", "Vesper", "Tania"] as Set
        // --8<-- [end:operators_plus_result]
    }
}
