package underdog.plots.dsl

import underdog.plots.ast.Node

/**
 * Common fields for Series
 *
 * @since 0.1.0
 */
@Node(hasId = true)
class Series {
    String type
    String name
    List data
    ItemStyle itemStyle
    Number yAxisIndex
    Number xAxisIndex
    Label label
    Number symbolSize
}
