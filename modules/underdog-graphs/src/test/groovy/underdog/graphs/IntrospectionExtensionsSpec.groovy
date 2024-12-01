package underdog.graphs

class IntrospectionExtensionsSpec extends BaseSpec {
    def "getting vertices degrees"() {
        when:
        def graph = loadKarateGraph()

        and: "getting the person with more relationships"
        def (name, age) = graph
            .degrees()
            .max { it.value }
            .key

        then:
        name == 'Pablo'
        age == 45
    }

    def "getting max degree vertex"() {
        when:
        def graph = loadKarateGraph()

        and: "getting the vertex with more relationships"
        def (name, age) = graph.maxDegree()

        then:
        name == 'Pablo'
        age == 45
    }
}
