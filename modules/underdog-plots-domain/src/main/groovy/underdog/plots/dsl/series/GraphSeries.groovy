package underdog.plots.dsl.series

import underdog.plots.ast.Node
import underdog.plots.dsl.series.graph.Force
import underdog.plots.dsl.Series

@Node
class GraphSeries extends Series {
    String type = 'graph'
    List links
    String layout
    Number symbolSize
    Force force
    List edgeSymbol
}
