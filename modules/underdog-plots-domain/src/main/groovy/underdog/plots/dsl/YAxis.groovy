package underdog.plots.dsl

import underdog.plots.ast.Node

@Node
class YAxis {
    String type
    String name
    String nameLocation
    Number nameGap
    Number gridIndex
    Number splitNumber
    List boundaryGap
    SplitLine splitLine
    NameTextStyle nameTextStyle
    Number min
    Number max
    String position
    Boolean scale
}
