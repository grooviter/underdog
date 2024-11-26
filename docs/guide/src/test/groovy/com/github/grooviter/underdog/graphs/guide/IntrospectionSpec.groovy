package com.github.grooviter.underdog.graphs.guide

import com.github.grooviter.underdog.graphs.Graphs
import spock.lang.Specification

class IntrospectionSpec extends Specification {
    def "maxDegree()"() {
        when:
        // tag::max_degree[]
        def graph = Graphs.graph(String) {
            ('A'..'C').each {
                vertex(it)
            }

            edge('A', 'B')
            edge('A', 'C')
        }

        def node = graph.maxDegree()
        // end::max_degree[]
        then:
        node == 'A'
    }

    // tag::person[]
    class Person {
        String name
        Integer age

        // this is used for destructuring eg:
        // def (name, age) = somePerson
        //       ^     ^
        //       |     |
        //       0     1
        Object getAt(Integer index) {
            return [name, age][index]
        }
    }
    // end::person[]
    def "person graph"() {
        when:
        // tag::person_graph[]
        def john = new Person(name: "John", age: 23)
        def elsa = new Person (name: "Elsa", age: 32)
        def raul = new Person(name: "Raul", age: 28)

        def graph = Graphs.digraph(Person) {
            vertex(john)
            vertex(elsa)
            vertex(raul)

            edge(john, elsa)
            edge(john, raul)
        }
        // end::person_graph[]

        // tag::person_max_degree[]
        def (name, age) = graph.maxDegree()
        // end::person_max_degree[]
        then:
        name == "John"
        age == 23
    }
}
