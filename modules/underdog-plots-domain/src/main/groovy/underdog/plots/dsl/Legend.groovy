package underdog.plots.dsl

import underdog.plots.ast.Node

@Node(hasId = true)
class Legend {
    String type
    boolean show
    Integer zlevel
    Integer z
    String left
    String top
    String right
    String bottom
    String width
    String height
    String orient
    String align
    Integer padding
    Integer itemGap
    Integer itemWidth
    Integer itemHeight
    // ItemStyle itemStyle
    // LineStyle lineStyle
    String symbolRotate
    String formatter
    Object selectedMode
    String inactiveColor
    String inactiveBorderColor
    String inactiveBorderWidth
    Map<String, Boolean> selected
    TextStyle textStyle
    // Tooltip tooltip
    String icon
    List data // array of legend

}
