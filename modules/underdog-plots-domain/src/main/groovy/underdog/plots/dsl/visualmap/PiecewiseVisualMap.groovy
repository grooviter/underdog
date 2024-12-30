package underdog.plots.dsl.visualmap

import underdog.plots.ast.Node
import underdog.plots.ast.RepeatableField
import underdog.plots.dsl.VisualMap

@Node
class PiecewiseVisualMap extends VisualMap {
    String type = 'piecewise'

    Number min
    Number max
    InRange inRange
    OutOfRange outOfRange

    @RepeatableField Pieces pieces
}
