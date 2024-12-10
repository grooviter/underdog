package underdog.plots.dsl.series

import underdog.plots.ast.Node
import underdog.plots.dsl.Series

@Node
class LineSeries extends Series {
    String type = 'line'
    Boolean smooth
}
