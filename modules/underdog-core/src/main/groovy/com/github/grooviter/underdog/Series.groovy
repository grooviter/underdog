package com.github.grooviter.underdog

import com.github.grooviter.underdog.series.DecimalSeries
import com.github.grooviter.underdog.series.StringSeries
import groovy.transform.NamedVariant

interface Series extends Columnar {

    Series plus(Integer number)
    Series plus(Series series)

    StringSeries getStr()

    @NamedVariant
    DecimalSeries toNumeric(String errors)
}