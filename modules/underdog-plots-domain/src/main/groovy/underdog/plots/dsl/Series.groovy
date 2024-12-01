package underdog.plots.dsl

import underdog.plots.ast.Node

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
