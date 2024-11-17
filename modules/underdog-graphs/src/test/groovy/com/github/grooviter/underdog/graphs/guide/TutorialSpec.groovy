package com.github.grooviter.underdog.graphs.guide

import com.github.grooviter.underdog.graphs.Graphs
import spock.lang.Specification

class TutorialSpec extends Specification {
    def "creation"() {
        setup:
        // tag::create[]
        def graph = Graphs.graph(String) {
            vertex("A")
            vertex("B")
            vertex("C")

            edge("A", "B")
            edge("A", "C")
        }
        // end::create[]
        expect:
        true
    }
}
