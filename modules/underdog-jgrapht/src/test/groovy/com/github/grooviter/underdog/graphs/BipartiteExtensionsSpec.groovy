package com.github.grooviter.underdog.graphs

class BipartiteExtensionsSpec extends BaseSpec {
    def "check: is bipartite"() {
        when:
        def basketball = loadBasketballGraph()

        then:
        basketball.isBipartite()
    }

    def "projected graph"() {
        when:
        def basketball = loadBasketballGraph()
        def projected = basketball.projectedGraph('A'..'C')

        then:
        projected.size() == 5
    }
}
