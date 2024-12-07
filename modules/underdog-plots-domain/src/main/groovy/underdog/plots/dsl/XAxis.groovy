package underdog.plots.dsl

import underdog.plots.ast.Node

@Node
class XAxis {
    String name
    String type
    String nameLocation
    Number nameGap
    Number gridIndex
    Number splitNumber
    List boundaryGap
    SplitLine splitLine
    AxisLabel axisLabel
    AxisLine axisLine
    NameTextStyle nameTextStyle
    Integer offset
    List data
    Number min
    Number max
    Boolean scale
}
