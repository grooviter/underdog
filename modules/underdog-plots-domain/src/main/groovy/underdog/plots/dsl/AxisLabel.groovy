package underdog.plots.dsl

import underdog.plots.ast.Node

@Node
class AxisLabel {
    boolean show = true
    String interval = 'auto'
    boolean inside
    Integer rotate
    Integer margin = 8
    String formatter
}
