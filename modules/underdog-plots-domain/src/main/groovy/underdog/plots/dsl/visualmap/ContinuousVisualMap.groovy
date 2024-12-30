package underdog.plots.dsl.visualmap

import underdog.plots.ast.Node
import underdog.plots.dsl.VisualMap

@Node
class ContinuousVisualMap extends VisualMap {
    String type = 'continuous'
    Boolean calculable
}
