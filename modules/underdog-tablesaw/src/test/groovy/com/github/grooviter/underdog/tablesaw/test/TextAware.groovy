package com.github.grooviter.underdog.tablesaw.test

trait TextAware {

    static String[] word(int size) {
        return (1..size).collect { "word" }
    }
}