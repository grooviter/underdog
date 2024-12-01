package underdog.graphs

class TraversalExtensionsSpec extends BaseSpec {
    def "iterators: depthfirst operator"() {
        setup:
        def school = loadSchoolGraph()
        def criteria = { it.name == 'Chris' && it.age < 30 }

        expect:
        school.'**'.find(criteria) == school.depthFirst().find(criteria)
    }
}
