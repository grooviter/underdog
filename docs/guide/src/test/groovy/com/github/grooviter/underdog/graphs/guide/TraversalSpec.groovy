package com.github.grooviter.underdog.graphs.guide

import com.github.grooviter.underdog.graphs.Graphs
import groovy.transform.Canonical
import spock.lang.Specification

class TraversalSpec extends Specification {

    @Canonical
    static class Person {
        String name
        Integer age
    }

    def "iterators: collections vertices"() {
        setup:
        // tag::collections_vertices[]
        def graph = Graphs.graph(Integer) {
            (1..10).each(delegate::vertex)
            edges(
                1, 3,
                    1, 5,
                    1, 7,
                    1, 9,
                    1, 2  // <--- looking here
            )
        }

        def answer1 = graph.vertices.findAll {
            graph.neighborsOf(it).any { it == 2 }
        }

        // end::collections_vertices[]
        expect:
        answer1.size() == 1
        answer1[0] == 1
    }

    def "iterators: collections edges"() {
        setup:
        // tag::collections_edges[]
        def anna = new Person("Anna", 24)
        def chris = new Person("Chris", 26)
        def paul = new Person("Paul", 30)
        def john = new Person("John", 28)

        def employees = Graphs.graph(Person) {
            // adding people
            [anna, chris, paul, john].each(delegate::vertex)
            edge(chris, paul, "boss")
            edge(anna, john, "boss")
            edge(chris, anna, "mentor")
        }

        def bossesNames = employees.edges                        // search in all edges where...
            .findAll { it.relation == 'boss' }            // boss-employee relationship
            .collect { employees.verticesOf(it)[0].name } // get only the boss name

        // end::collections_edges[]
        expect:
        bossesNames == ['Chris', 'Anna']
    }

    def "iterators: depthfirst operator"() {
        setup:
        // tag::depth_first[]
        def anna = new Person("Anna", 24)
        def chris = new Person("Chris", 26)
        def paul = new Person("Paul", 30)
        def john = new Person("John", 28)

        def dates = Graphs.graph(Person){
            // adding people
            [anna, chris, paul, john].each(delegate::vertex)

            // adding dates
            edges(
                anna, paul,
                anna, chris,
                chris, paul,
                anna, john
            )
        }

        def answer = dates.'**'.find { Person person ->
            // less than 30 years
            person.age < 30 &&
            // have dated John
            dates.neighborsOf(person).any { p -> p.name == "John"}
        }
        // end::depth_first[]
        expect:
        answer == anna
    }
}
