package memento.plots.extensions

import com.github.grooviter.underdog.DataFrame
import com.github.grooviter.underdog.plots.Options
import memento.plots.Plots

class DataFrameExtensions {
    static Options plotLine(DataFrame df){
        return Plots.line(df['x'] as List<Number>, df['y'] as List<Number>, df.name)
    }
}
