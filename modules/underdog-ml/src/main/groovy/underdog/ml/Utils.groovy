package underdog.ml

import groovy.transform.NamedParam
import groovy.transform.NamedVariant
import org.codehaus.groovy.runtime.DefaultGroovyMethods
import smile.data.DataFrame
import smile.data.vector.BaseVector
import smile.data.vector.IntVector

class Utils {
    @NamedVariant
    Tuple4<double[][], double[][], int[], int[]> trainTestSplit(
            double[][] X,
            int[] y,
            @NamedParam(required = false) long random_state = 0,
            @NamedParam(required = false) boolean shuffle = false,
            @NamedParam(required = false) double train_size = 0.5) {

        def samplesLength = X.length
        def indexes = (0..<samplesLength).with {
            shuffle ? it.shuffled(new Random(random_state ?: 0)) : it
        }

        def trainSize = (samplesLength * train_size).toInteger()
        def testSize = samplesLength - trainSize
        def (double[][] xTrain, double[][] xTest) = DefaultGroovyMethods.chop(X, trainSize, testSize)
        def (int[] yTrain, int[] yTest) = DefaultGroovyMethods.chop(y as Number[], trainSize, testSize)

        return [xTrain, xTest, yTrain, yTest]
    }

    @NamedVariant
    Tuple4<double[][], double[][], double[], double[]> trainTestSplit(
            double[][] X,
            double[] y,
            @NamedParam(required = false) long random_state = 0,
            @NamedParam(required = false) boolean shuffle = false,
            @NamedParam(required = false) double train_size = 0.5) {

        def samplesLength = X.length
        def indexes = (0..<samplesLength).with {
            shuffle ? it.shuffled(new Random(random_state ?: 0)) : it
        }

        def trainSize = (samplesLength * train_size).toInteger()
        def testSize = samplesLength - trainSize
        def (double[][] xTrain, double[][] xTest) = DefaultGroovyMethods.chop(X, trainSize, testSize)
        def (double[] yTrain, double[] yTest) = DefaultGroovyMethods.chop(y as Number[], trainSize, testSize)

        return [xTrain, xTest, yTrain, yTest]
    }

    /**
     * This method returns a {@link smile.data.DataFrame} from a features (X) and labels (y) arrays. All the
     * feature columns will have names from X0...Xn and the last column of the dataframe will be "y". All feature
     * columns will be of type double whereas column "y" will have int type.
     *
     * @param X features
     * @param y labels
     * @return {@link smile.data.DataFrame}
     * @since 0.1.0
     */
    static DataFrame createDataFrameFrom(double[][] X, int[] y) {
        DataFrame dataFrame = createDataFrameFromNumberArray(X, y as Number[])

        List<BaseVector> vectorList = (0..<dataFrame.ncol() - 1).collect { int colIndex -> dataFrame.column(colIndex)}
        BaseVector yAsDoubleVector = dataFrame.column(dataFrame.ncol() - 1)
        BaseVector yAsIntegerVector = IntVector.of(
                yAsDoubleVector.name(),
                yAsDoubleVector.toDoubleArray()*.intValue() as int[])

        return DataFrame.of([*vectorList, yAsIntegerVector] as BaseVector[])
    }

    static DataFrame createDataFrameFrom(double[][] X) {
        String[] names = (0..<X[0].length).collect { "X$it" }
        return DataFrame.of(X, names)
    }

    /**
     * This method returns a {@link smile.data.DataFrame} from a features (X) and labels (y) arrays. All the
     * feature columns will have names from X0...Xn and the last column of the dataframe will be "y". All columns
     * will be of type double
     *
     * @param X features
     * @param y labels
     * @return {@link smile.data.DataFrame}
     * @since 0.1.0
     */
    static DataFrame createDataFrameFrom(double[][] X, double[] y) {
        return createDataFrameFromNumberArray(X, y as Number[])
    }

    private static DataFrame createDataFrameFromNumberArray(double[][] X, Number[] y) {
        String[] names = (0..<X[0].length).collect { "X$it" } + 'y'
        double[][] df = [X, y].transpose()*.flatten() as double[][]
        return DataFrame.of(df, names)
    }
}
