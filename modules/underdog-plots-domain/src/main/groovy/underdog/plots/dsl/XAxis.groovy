package underdog.plots.dsl

import underdog.plots.ast.Node

@Node
class XAxis {
    boolean show
    String name
    String type
    String nameLocation
    String position
    Number nameGap
    Number gridIndex
    Boolean inverse
    Number splitNumber
    List boundaryGap
    SplitLine splitLine
    SplitArea splitArea
    NameTextStyle nameTextStyle
    NameTruncate nameTruncate
    Integer offset
    List data
    Number min
    Number max
    Boolean scale
    AxisTick axisTick
    AxisLabel axisLabel
    AxisLine axisLine
}
