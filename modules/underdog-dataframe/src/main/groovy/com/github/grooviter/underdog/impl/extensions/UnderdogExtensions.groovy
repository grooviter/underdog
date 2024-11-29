package com.github.grooviter.underdog.impl.extensions

import com.github.grooviter.underdog.DataFrames
import com.github.grooviter.underdog.Underdog

class UnderdogExtensions {
    static DataFrames df(Underdog underdog) {
        return new DataFrames()
    }
}
