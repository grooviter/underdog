package com.github.grooviter.underdog.plots.dsl

import com.github.grooviter.underdog.plots.ast.Node

@Node
class YAxis {
    String type
    String name
    String nameLocation
    NameTextStyle nameTextStyle
    Number min
    Number max
    String position
    Boolean scale
}
