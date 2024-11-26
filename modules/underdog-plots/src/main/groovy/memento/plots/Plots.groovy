package memento.plots

import com.github.grooviter.underdog.plots.Options
import groovy.transform.NamedParam
import groovy.transform.NamedVariant
import memento.plots.charts.Graph
import memento.plots.charts.Histogram
import memento.plots.charts.Line
import memento.plots.charts.Scatter

class Plots {
    @Delegate Line line = new Line()
    @Delegate Scatter scatter = new Scatter()
    @Delegate Histogram histogram = new Histogram()
    @Delegate Graph graphDelegate = new Graph()

    @NamedVariant
    Options plot(
        List<Number> x,
        List<Number> y,
        @NamedParam(required = false, value='title') String chartTitle = "",
        @NamedParam(required = false) boolean smooth = false) {
        return line(x, y, chartTitle: chartTitle, smooth: smooth)
    }

    @NamedVariant
    Options plot(
        List<Number> xs,
        Map<String, List<Number>> ys,
        @NamedParam(required = false, value='title') String chartTitle = "") {
        return lines(xs, ys, chartTitle: chartTitle)
    }

    Options plot(
        List<Number> x,
        List<Number> y,
        @DelegatesTo(Options) Closure options){
        return line(x, y, options)
    }

    String show(Options options) {
        return new Render().render(options)
    }

    static Plots plots() {
        return new Plots()
    }
}
