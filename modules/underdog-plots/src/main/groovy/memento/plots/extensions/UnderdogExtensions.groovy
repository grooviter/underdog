package memento.plots.extensions

import com.github.grooviter.underdog.Underdog
import memento.plots.Plots

class UnderdogExtensions {
    static Plots plots(Underdog underdog) {
        return Plots.plots()
    }
}
