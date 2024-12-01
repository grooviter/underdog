package underdog.plots.dsl

import underdog.plots.ast.Node

@Node(hasId = true)
class Grid {
    boolean show
    Integer zlevel
    Integer z
    String left
    String top
    String right
    String bottom
    String width
    String height
    boolean containLabel
    String backgroundColor
    String borderColor
    Integer borderWidth
    Integer shadowBlur
    String shadowColor
    Integer shadowOffsetX
    Integer shadowOffsetY
    // Tooltip tooltip
}
