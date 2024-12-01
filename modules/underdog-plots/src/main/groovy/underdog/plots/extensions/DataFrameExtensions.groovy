package underdog.plots.extensions

import com.github.grooviter.underdog.DataFrame
import com.github.grooviter.underdog.Series
import com.github.grooviter.underdog.plots.Options
import underdog.plots.Plots

/**
 * This class adds methods to {@link DataFrame} instances to make it easier to render plots directly
 * from {@link DataFrame} instances.
 *
 * @since 0.1.0
 */
class DataFrameExtensions {
    /**
     * Creates a line chart from a given {@link DataFrame} with a Series named X and a
     * Series named Y
     *
     * @param df a {@link DataFrame}
     * @return an instance of {@link Options}
     * @since 0.1.0
     */
    static Options line(DataFrame df){
        def (x, y) = extractXAndYSeries(df)
        return Plots.plots().line(x, y)
    }

    /**
     * Creates a scatter chart from a given {@link DataFrame} containing a Series named X
     * and a Series named Y
     *
     * @param df a {@link DataFrame}
     * @return an instance of {@link Options}
     * @since 0.1.0
     */
    static Options scatter(DataFrame df) {
        def (x, y) = extractXAndYSeries(df)
        return Plots.plots().scatter(x, y)
    }

    /**
     * Creates a scatter chart from a given {@link DataFrame} containing two Series with
     * the names passed as parameter for X and Y axis
     *
     * @param df a {@link DataFrame}
     * @param xName Series name for the X axis
     * @param yName Series name for the Y axis
     * @return an instance of {@link Options}
     * @since 0.1.0
     */
    static Options scatter(DataFrame df, String xName, String yName) {
        return Plots.plots().scatter(df[xName], df[yName], title: df.name)
    }

    /**
     * Creates a scatter chart from a given {@link DataFrame} containing two Series with
     * the names passed as parameter for X and Y axis.
     *
     * @param xName Series name for the X axis
     * @param yName Series name for the Y axis
     * @param groupName Series name for the scatter grouping
     * @param df a {@link DataFrame}
     * @return an instance of {@link Options}
     * @since 0.1.0
     */
    static Options scatter(DataFrame df, String xName, String yName, String groupName) {
        return Plots.plots().scatter(df[xName], df[yName], df[groupName], df.name)
    }

    private static List<Series> extractXAndYSeries(DataFrame df) {
        List<Series> series = df.columns
            .findAll { it.toLowerCase() in ['x', 'y'] }
            .collect { df[it] }

        assert series.size() == 2, "Haven't found Series X and Y in dataframe"
        return series
    }
}
