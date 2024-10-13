package com.github.grooviter.underdog.graphs

import groovy.transform.Canonical

@Canonical
class Person {
    String name
    Integer age
    Object getAt(Integer index) {
        return this.properties.values().indexed()[index]
    }
}
