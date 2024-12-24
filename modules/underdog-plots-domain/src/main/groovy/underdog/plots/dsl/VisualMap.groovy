package underdog.plots.dsl

import underdog.plots.ast.Node

@Node
class VisualMap {
    Number min
    Number max
    Boolean calculable
    String orient
    String left
    String bottom
    Boolean show
}
