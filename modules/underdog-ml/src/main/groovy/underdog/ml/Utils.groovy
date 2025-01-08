package underdog.ml

import groovy.transform.CompileStatic
import groovy.transform.NamedParam
import groovy.transform.NamedVariant
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
        def (
            double[][] xTrain,
            double[][] xTest,
            int[] yTrain,
            int[] yTest
        ) = trainTestSplitBoxed(X as Double[][], y as Integer[], random_state, shuffle, train_size)

        return [xTrain, xTest, yTrain, yTest]
    }

    @NamedVariant
    Tuple4<double[][], double[][], double[], double[]> trainTestSplit(
        double[][] X,
        double[] y,
        @NamedParam(required = false) long random_state = 0,
        @NamedParam(required = false) boolean shuffle = true,
        @NamedParam(required = false) double train_size = 0.75) {
        def (
            double[][] xTrain,
            double[][] xTest,
            double[] yTrain,
            double[] yTest
        ) = trainTestSplitBoxed(X as Double[][], y as Double[], random_state, shuffle, train_size)

        return [xTrain, xTest, yTrain, yTest]
    }

    private static <X,Y> Tuple4<X[][], X[][], Y[], Y[]> trainTestSplitBoxed(
            X[][] x,
            Y[] y,
            long random_state = 0,
            boolean shuffle = true,
            double train_size = 0.75) {
        def samples = createTuple(x, y)

        if (shuffle) {
            samples = samples.shuffled(new Random(random_state)) as Tuple2<X[], Y>[]
        }

        def trainSize = (samples.size() * train_size).toInteger()
        def testSize = samples.size() - trainSize

        def (trainSet, testSet) = samples.<Tuple2<X[], Y>>chop(trainSize, testSize)

        return new Tuple4<X[][], X[][], Y[], Y[]>(
            extractX(trainSet) as X[][],
            extractX(testSet) as X[][],
            extractY(trainSet) as Y[],
            extractY(testSet) as Y[]
        )
    }

    @CompileStatic
    private static <X,Y> Tuple2<X[], Y>[] createTuple(X[][] x, Y[] y) {
        return [x, y].transpose() as Tuple2<X[], Y>[]
    }

    @CompileStatic
    private static <X,Y> X[][] extractX(List<Tuple2<X[], Y>> data) {
        return data.<Tuple2<X[],Y>,X[]>collect { Tuple2<X[], Y> t ->
            return t.v1 as X[]
        } as X[][]
    }

    @CompileStatic
    private static <X,Y> Y[] extractY(List<Tuple2<X[], Y>> data) {
        return data.<Tuple2<X[],Y>,Y>collect { Tuple2<X[], Y> t ->
            return t.v2
        } as Y[]
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
