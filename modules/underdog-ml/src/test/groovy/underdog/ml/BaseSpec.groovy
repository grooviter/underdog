package underdog.ml

import underdog.DataFrame
import underdog.Underdog
import spock.lang.Specification

class BaseSpec extends Specification {
    DataFrame loadFoodDataFrame() {
        return Underdog.df()
                .read_csv("src/test/resources/data/food.csv", sep: ";")
                .dropna()
    }

    Tuple4<double[][], double[][], int[], int[]> binaryClassificationTrainTestSplit(List<Integer> classes = [1, -1]) {
        def (on, off) = classes
        def df = Underdog.df()
            .read_csv("src/test/resources/data/food.csv", sep: ";")
            .dropna()

        df['y'] = df['TRAFFICLIGHT VALUE'](Integer, Integer){it == 3 ? on : off }

        def X = df[['CARBS', 'SUGAR', 'PROTEINS', 'FAT', 'SALT', 'FIBER']] as double[][]
        def y = df['y'] as int[]

        return ML.utils.trainTestSplit(X, y)
    }

    Tuple4<double[][], double[][], int[], int[]> multiClassificationTrainTestSplit() {
        def df = Underdog.df()
                .read_csv("src/test/resources/data/food.csv", sep: ";")
                .dropna()

        df['y'] = df['TRAFFICLIGHT VALUE']

        def X = df[['CARBS', 'SUGAR', 'PROTEINS', 'FAT', 'SALT', 'FIBER']] as double[][]
        def y = df['y'] as int[]

        return ML.utils.trainTestSplit(X, y)
    }
}
