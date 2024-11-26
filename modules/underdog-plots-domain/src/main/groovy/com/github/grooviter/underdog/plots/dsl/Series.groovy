package com.github.grooviter.underdog.plots.dsl

import com.github.grooviter.underdog.plots.ast.Node

@Node(hasId = true)
class Series {
    String type
    String name
    List data
    ItemStyle itemStyle

    Boolean smooth
    String barWidth

    Label label

    // Graph
    List links
    String layout
    Number symbolSize
    Force force
    List edgeSymbol
}
