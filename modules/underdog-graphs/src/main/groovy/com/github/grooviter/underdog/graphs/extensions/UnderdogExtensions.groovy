package com.github.grooviter.underdog.graphs.extensions

import com.github.grooviter.underdog.Underdog
import com.github.grooviter.underdog.graphs.Graphs

class UnderdogExtensions {
    static Graphs graphs(Underdog underdog) {
        return new Graphs()
    }
}
