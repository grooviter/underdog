package com.github.grooviter.underdog.tablesaw.test

trait NumericAware {

    static int[] zeros(Integer size) {
        return (1..size).collect { 0 }
    }

    static double[] zerosAsDouble(Integer size) {
        return (1..size).collect { 0  as Double }
    }
}
