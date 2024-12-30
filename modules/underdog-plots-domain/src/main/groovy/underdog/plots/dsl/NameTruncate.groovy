package underdog.plots.dsl

import underdog.plots.ast.Node

@Node
class NameTruncate {
    Number maxWidth
    String ellipsis
    Boolean inverse
}
