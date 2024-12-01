package underdog.plots.dsl

import underdog.plots.ast.Node

@Node
class YAxis {
    String type
    String name
    String nameLocation
    NameTextStyle nameTextStyle
    Number min
    Number max
    String position
    Boolean scale
}
