package com.github.grooviter.underdog

import groovy.transform.Canonical
import org.codehaus.groovy.runtime.DefaultGroovyMethods

@Canonical
class Shape {
    int rows, cols

    int getAt(Integer rowOrCol) {
        return [rows, cols].get(rowOrCol)
    }

    @Override
    String toString() {
        return "$rows rows X $cols cols"
    }
}
