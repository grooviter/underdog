package underdog.plots.dsl

import underdog.plots.ast.Node

@Node
class YAxis {
    boolean show
    String type
    String name
    String nameLocation
    Boolean inverse
    Number nameGap
    Number gridIndex
    Number splitNumber
    List boundaryGap
    List data
    SplitLine splitLine
    NameTextStyle nameTextStyle
    Number min
    Number max
    String position
    Boolean scale
    AxisTick axisTick
    AxisLabel axisLabel
}
