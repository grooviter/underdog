package memento.plots.extensions

import com.github.grooviter.underdog.DataFrame
import com.github.grooviter.underdog.plots.Options
import memento.plots.Plots

/**
 * This class adds methods to {@link DataFrame} instances to make it easier to render plots directly
 * from {@link DataFrame} instances.
 *
 * @since 0.1.0
 */
class DataFrameExtensions {
    static Options plotLine(DataFrame df){
        return Plots.plots().line(df['x'], df['y'])
    }
}
