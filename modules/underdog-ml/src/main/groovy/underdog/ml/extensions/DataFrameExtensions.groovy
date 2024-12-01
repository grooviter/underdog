package underdog.ml.extensions

import groovy.util.logging.Slf4j
import underdog.DataFrame
import underdog.ml.ML

import java.util.function.Function

/**
 * Methods added to {@link DataFrame} instances using functions added in the ml module
 *
 * @since 0.1.0
 */
@Slf4j
class DataFrameExtensions {

    /**
     * Normalizes series included in the {@link DataFrame} passed as parameter using a standardize scaler which
     * standardize features by removing the mean and scaling to unit variance.
     *
     * @param dataFrame data to normalize
     * @return a new {@link DataFrame} instance with the same series but normalized
     * @since 0.1.0
     */
    static DataFrame standardScale(DataFrame dataFrame) {
        return executeScale(dataFrame, (values) -> ML.features.standardizeScaler(values).apply(values))
    }

    /**
     * Normalizes series included in the {@link DataFrame} passed as parameter using a min max scaler which
     * transform features by scaling each feature to a given range.
     *
     * @param dataFrame data to normalize
     * @return a new {@link DataFrame} instance with the same series but normalized
     * @since 0.1.0
     */
    static DataFrame minMaxScale(DataFrame dataFrame) {
        return executeScale(dataFrame, (values) -> ML.features.minMaxScaler(values).apply(values) )
    }

    private static executeScale(DataFrame dataFrame, Function<double[][], double[][]> fn) {
        double[][] values = [] as double[][]

        try {
            values = dataFrame as double[][]
        } catch (Throwable th) {
            log.error("can't scale dataframe values, check all series are numeric", th)
        }

        if (!values) {
            return dataFrame
        }

        double[][] scaled = fn.apply(values)

        dataFrame.columns.eachWithIndex { String col, Integer ix ->
            List colValues = scaled.collect {it[ix] }
            dataFrame[col] = colValues
        }

        return dataFrame
    }
}
