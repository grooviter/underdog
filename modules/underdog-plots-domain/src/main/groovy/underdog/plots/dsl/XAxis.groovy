package underdog.plots.dsl

import underdog.plots.ast.Node

@Node
class XAxis {
    boolean show
    String name
    String type
    String nameLocation
    Number nameGap
    Number gridIndex
    Number splitNumber
    List boundaryGap
    SplitLine splitLine
    NameTextStyle nameTextStyle
    Integer offset
    List data
    Number min
    Number max
    Boolean scale
    AxisTick axisTick
    AxisLabel axisLabel
    AxisLine axisLine
}
