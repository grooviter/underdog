package underdog.plots.dsl.series

import underdog.plots.ast.Node
import underdog.plots.dsl.Series

@Node
class GraphSeries extends Series {
    String type = 'graph'
}
