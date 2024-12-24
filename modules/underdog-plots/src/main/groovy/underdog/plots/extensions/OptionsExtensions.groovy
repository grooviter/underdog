package underdog.plots.extensions

import underdog.plots.Options
import groovy.json.JsonOutput
import underdog.plots.Plots
import underdog.plots.Render
import underdog.plots.Render.Meta

/**
 * This class adds custom methods to the {@link Options} instance. Many of those methods couldn't be added in
 * the domain module as they couldn't have been statically compile there.
 *
 * @since 0.1.0
 */
class OptionsExtensions {

    /**
     * Renders a given {@link Options} instance as JSON
     *
     * @param options the {@link Options} instance to render
     * @return a {@link String} with the JSON representation
     * @since 0.1.0
     */
    static String toJson(Options options, boolean pretty = false) {
        String json = JsonOutput.toJson(options.map)
        if (pretty) {
            return JsonOutput.prettyPrint(json)
        }
        return json
    }

    /**
     * Helper method inside an {@link Options} instance to convert some {@link Map}
     * to JSON string. It could be useful to render some extra
     *
     * @param options current {@link Options} instance
     * @param mapToJson {@link Map} we want to convert to JSON
     * @return a string representation of the map passed as parameter
     * @since 0.1.0
     */
    static String toJson(Options options, Map mapToJson) {
        return JsonOutput.toJson(mapToJson)
    }

    /**
     * Adds up two {@link Options} instances
     *
     * @param options the base {@link Options}
     * @param other the {@link Options} to add
     * @return the sum of the two previous {@link Options}
     * @since 0.1.0
     */
    static Options plus(Options options, Options other) {
        def result = new Options()
        result.map = options.map + other.map
        return result
    }

    /**
     * Renders the current {@link Options} instance
     *
     * @param options the {@link Options} to render
     * @return the {@link String} representation of the rendering
     * @since 0.1.0
     */
    static String show(Options options) {
        return Plots.plots().show(options)
    }

    /**
     * Renders the current {@link Options} instance
     *
     * @param options the {@link Options} to render
     * @param meta
     * @return the {@link String} representation of the rendering
     * @since 0.1.0
     */
    static String show(Options options, Meta meta) {
        return Plots.plots().show(options, meta)
    }

    /**
     * Customizes a given {@link Options} instance with extra options using the options dsl
     *
     * @param options the {@link Options} instance to customize
     * @param dsl the extra options using the {@link Options} dsl
     * @return the sum of the instance to customize and the customizations
     * @since 0.1.0
     */
    static Options customize(
        Options options,
        @DelegatesTo(value=Options, strategy = Closure.DELEGATE_FIRST) Closure dsl) {
        return options + Options.create(dsl)
    }

    static List toData(Options options, Number[][] x, Number[] y, boolean sortByX = true) {
        def data = [
            x.collect().flatten(),  // xs
            y.collect()             // ys
        ].transpose()

        if (sortByX) {
            return data.sort { it.find() } // [[xs1,ys1]...[xsn,ysn]]
        }

        return data
    }

    static List toData(Options options, double[][] x, double[] y, boolean sortByX = true) {
        return toData(options, x as Double[][], y as Double[], sortByX)
    }

    static List toData(Options options, double[][] x, int[] y, boolean sortByX = true) {
        return toData(options, x as Double[][], y as Integer[], sortByX)
    }

    static List toData(Options options, List<Number> x, double[] y, boolean sortByX = true){
        return toData(options, x as Number[][], y as Number[], sortByX)
    }

    static double[][] sortX(Options options, double[][] x) {
        return x.collect().flatten().sort() as double[][]
    }

    static double[] sortX(Options options, double[] x) {
        return x.collect().sort() as double[]
    }
}
