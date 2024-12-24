package underdog.plots

import groovy.transform.NamedParam
import groovy.transform.NamedVariant
import underdog.plots.Render.Meta
import underdog.plots.charts.Bar
import underdog.plots.charts.Graph
import underdog.plots.charts.CorrelationMatrix
import underdog.plots.charts.Histogram
import underdog.plots.charts.Line
import underdog.plots.charts.Pie
import underdog.plots.charts.Radar
import underdog.plots.charts.Scatter
import underdog.plots.charts.ScatterMatrix

class Plots {
    @Delegate Line lineDelegate = new Line()
    @Delegate Scatter scatterDelegate = new Scatter()
    @Delegate Histogram histogram = new Histogram()
    @Delegate Graph graphDelegate = new Graph()
    @Delegate Bar barDelegate = new Bar()
    @Delegate Pie pieDelegate = new Pie()
    @Delegate Radar radarDelegate = new Radar()
    @Delegate ScatterMatrix scatterMatrixDelegate = new ScatterMatrix()
    @Delegate CorrelationMatrix heatmapDelegate = new CorrelationMatrix()


    @NamedVariant
    static String show(
        Options options,
        @NamedParam(required = false) String width = "800px",
        @NamedParam(required = false) String height = "600px",
        @NamedParam(required = false) String theme = ""
    ) {
        return new Render().render(options, Meta.builder().width(width).height(height).theme(theme).build())
    }

    String show(Options options, Meta meta) {
        return new Render().render(options, meta)
    }

    static Plots plots() {
        return new Plots()
    }
}
