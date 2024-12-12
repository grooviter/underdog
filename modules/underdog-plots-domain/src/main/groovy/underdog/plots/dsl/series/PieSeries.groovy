package underdog.plots.dsl.series

import underdog.plots.ast.Node
import underdog.plots.dsl.Series

@Node
class PieSeries extends Series {
    String type = 'pie'
    String radius
}
