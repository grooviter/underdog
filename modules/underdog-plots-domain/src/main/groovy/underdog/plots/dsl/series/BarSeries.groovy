package underdog.plots.dsl.series

import underdog.plots.ast.Node
import underdog.plots.dsl.Series

@Node
class BarSeries extends Series {
    String type = 'bar'
}
