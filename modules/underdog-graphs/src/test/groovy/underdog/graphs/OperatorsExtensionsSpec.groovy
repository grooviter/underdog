package underdog.graphs

class OperatorsExtensionsSpec extends BaseSpec {
    def "graph union: plus operator"() {
        setup:
        def school = loadSchoolGraph()
        def karate = loadKarateGraph()

        when:
        def neighborhood = school + karate

        then:
        neighborhood.vertexSet().size() == 5
    }
}
