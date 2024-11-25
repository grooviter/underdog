package com.github.grooviter.underdog.plots

import com.github.grooviter.underdog.plots.ast.Node
import com.github.grooviter.underdog.plots.ast.RepeatableField
import com.github.grooviter.underdog.plots.dsl.Grid
import com.github.grooviter.underdog.plots.dsl.Legend
import com.github.grooviter.underdog.plots.dsl.Series
import com.github.grooviter.underdog.plots.dsl.Title
import com.github.grooviter.underdog.plots.dsl.XAxis
import com.github.grooviter.underdog.plots.dsl.YAxis
import groovy.json.JsonOutput
import groovy.transform.CompileDynamic

@Node
class Options {
    Title title

    @RepeatableField Legend legend
    @RepeatableField Grid grid
    @RepeatableField XAxis xAxis
    @RepeatableField YAxis yAxis
    @RepeatableField Series series

    static Options create(@DelegatesTo(value=Options, strategy = Closure.DELEGATE_FIRST) Closure dsl) {
        return new Options().tap(dsl)
    }
}
