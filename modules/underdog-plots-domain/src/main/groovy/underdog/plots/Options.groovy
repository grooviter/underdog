package underdog.plots

import underdog.plots.ast.Node
import underdog.plots.ast.RepeatableField
import underdog.plots.dsl.AxisPointer
import underdog.plots.dsl.Grid
import underdog.plots.dsl.Legend
import underdog.plots.dsl.Series
import underdog.plots.dsl.Title
import underdog.plots.dsl.Tooltip
import underdog.plots.dsl.XAxis
import underdog.plots.dsl.YAxis

@Node
class Options {
    Title title
    Tooltip tooltip
    AxisPointer axisPointer

    @RepeatableField Legend legend
    @RepeatableField Grid grid
    @RepeatableField XAxis xAxis
    @RepeatableField YAxis yAxis
    @RepeatableField Series series

    static Options create(@DelegatesTo(value=Options, strategy = Closure.DELEGATE_FIRST) Closure dsl) {
        return new Options().tap(dsl)
    }
}
