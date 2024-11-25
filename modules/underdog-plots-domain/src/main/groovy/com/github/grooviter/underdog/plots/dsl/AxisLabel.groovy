package com.github.grooviter.underdog.plots.dsl

import com.github.grooviter.underdog.plots.ast.Node

@Node
class AxisLabel {
    boolean show = true
    String interval = 'auto'
    boolean inside
    Integer rotate
    Integer margin = 8
    String formatter
}
