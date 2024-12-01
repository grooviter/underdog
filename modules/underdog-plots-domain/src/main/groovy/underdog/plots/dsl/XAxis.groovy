package underdog.plots.dsl

import underdog.plots.ast.Node

@Node
class XAxis {
    String name
    String type
    String nameLocation
    NameTextStyle nameTextStyle
    Integer offset
    List data
    Number min
    Number max
    Boolean scale
}
