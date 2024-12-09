package underdog.impl.extensions

import tech.tablesaw.columns.numbers.Stats
import underdog.DataFrame
import underdog.Series
import underdog.impl.TSDataFrame

import java.math.MathContext
import java.math.RoundingMode

/**
 * Statistical methods available in {@link Series} objects
 *
 * @since 0.1.0
 */
class SeriesStatsExtensions extends SeriesExtensionsBase {

    static Double mean(Series source, int precision = 0) {
        assert isNumericColumn(source), "Can't resolve stats from a non numeric column"
        double mean = getStats(source).mean()
        return precision > 0 ? usePrecision(mean, precision) : mean
    }

    static Double meanGeometric(Series source){
        assert isNumericColumn(source), "Can't resolve stats from a non numeric column"
        return getStats(source).geometricMean()
    }

    static Double avg(Series source){
        return mean(source)
    }

    static Double meanQuadratic(Series source){
        assert isNumericColumn(source), "Can't resolve stats from a non numeric column"
        return getStats(source).quadraticMean()
    }

    static Double min(Series source){
        assert isNumericColumn(source), "Can't resolve stats from a non numeric column"
        return getStats(source).min()
    }

    static Double max(Series source){
        assert isNumericColumn(source), "Can't resolve stats from a non numeric column"
        return getStats(source).max()
    }

    static Double populationVariance(Series source){
        assert isNumericColumn(source), "Can't resolve stats from a non numeric column"
        return getStats(source).populationVariance()
    }

    static Double range(Series source){
        assert isNumericColumn(source), "Can't resolve stats from a non numeric column"
        return getStats(source).range()
    }

    static Double secondMoment(Series source){
        assert isNumericColumn(source), "Can't resolve stats from a non numeric column"
        return getStats(source).secondMoment()
    }

    static DataFrame stats(Series source) {
        assert isNumericColumn(source), "Can't resolve stats from a non numeric column"
        return new TSDataFrame(getStats(source).asTable())
    }

    static Double std(Series source) {
        assert isNumericColumn(source), "Can't resolve stats from a non numeric column"
        return getStats(source).standardDeviation()
    }

    static Double sum(Series source){
        assert isNumericColumn(source), "Can't resolve stats from a non numeric column"
        return getStats(source).sum()
    }

    static Double sumOfLogs(Series source){
        assert isNumericColumn(source), "Can't resolve stats from a non numeric column"
        return getStats(source).sumOfLogs()
    }

    static Double sumOfSquares(Series source){
        assert isNumericColumn(source), "Can't resolve stats from a non numeric column"
        return getStats(source).sumOfSquares()
    }

    static Double variance(Series source){
        assert isNumericColumn(source), "Can't resolve stats from a non numeric column"
        return getStats(source).variance()
    }

    private static Stats getStats(Series source) {
        return getAsNumericColumn(source).stats()
    }

    private static Double usePrecision(double number, int precision = 7) {
        return new BigDecimal(number, new MathContext(precision, RoundingMode.HALF_EVEN))
    }
}
