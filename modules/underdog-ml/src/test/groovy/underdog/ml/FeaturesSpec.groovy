package underdog.ml

class FeaturesSpec extends BaseSpec {

    def "PCA"() {
        setup:
        def df = loadFoodDataFrame()
        df['y'] = df['TRAFFICLIGHT VALUE'](Integer, Integer){it == 3 ? 1 : 0 }

        when:
        def X = df[['CARBS', 'SUGAR', 'PROTEINS', 'FAT', 'SALT', 'FIBER']] as double[][]
        def y = df['y'] as int[]

        and:
        def xReduced = ML.features.pca(X, nComponents: 2).apply(X)

        then:
        X.shape() == [1014, 6]

        and:
        xReduced.shape() == [1014, 2]
    }
}
