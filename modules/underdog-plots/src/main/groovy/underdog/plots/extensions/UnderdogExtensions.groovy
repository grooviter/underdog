package underdog.plots.extensions

import com.github.grooviter.underdog.Underdog
import underdog.plots.Plots

class UnderdogExtensions {
    static Plots plots(Underdog underdog) {
        return Plots.plots()
    }
}
