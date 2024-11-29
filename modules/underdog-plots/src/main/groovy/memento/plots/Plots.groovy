package memento.plots

import com.github.grooviter.underdog.plots.Options
import groovy.transform.NamedParam
import groovy.transform.NamedVariant
import memento.plots.charts.Bar
import memento.plots.charts.Graph
import memento.plots.charts.Histogram
import memento.plots.charts.Line
import memento.plots.charts.Scatter

class Plots {
    @Delegate Line line = new Line()
    @Delegate Scatter scatter = new Scatter()
    @Delegate Histogram histogram = new Histogram()
    @Delegate Graph graphDelegate = new Graph()
    @Delegate Bar barDelegate = new Bar()

    @NamedVariant
    Options plot(
        List<Number> x,
        List<Number> y,
        @NamedParam(required = false, value='title') String chartTitle = "",
        @NamedParam(required = false) boolean smooth = false) {
        return line.line(x, y, title: chartTitle, smooth: smooth)
    }

    @NamedVariant
    Options plot(
        Map<String, List<Number>> data,
        @NamedParam(required = false, value='title') String chartTitle = "") {
        return line.lines(data, title: chartTitle)
    }

    String show(Options options) {
        return new Render().render(options)
    }

    static Plots plots() {
        return new Plots()
    }
}
