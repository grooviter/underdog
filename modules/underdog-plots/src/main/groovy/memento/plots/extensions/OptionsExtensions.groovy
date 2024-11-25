package memento.plots.extensions

import com.github.grooviter.underdog.plots.Options
import groovy.json.JsonOutput
import memento.plots.Plots
import memento.plots.Render

class OptionsExtensions {
    static String toJson(Options options, boolean pretty = false) {
        String json = JsonOutput.toJson(options.map)
        if (pretty) {
            return JsonOutput.prettyPrint(json)
        }
        return json
    }

    static Options plus(Options options, Options other) {
        def result = new Options()
        result.map = options.map + other.map
        return result
    }

    static String show(Options options) {
        return Plots.plots().show(options)
    }
}
