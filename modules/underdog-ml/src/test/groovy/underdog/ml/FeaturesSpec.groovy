package underdog.ml

class FeaturesSpec extends BaseSpec {

    def "PCA"() {
        setup:
        def df = loadFoodDataFrame()

        when:
        def X = df[['CARBS', 'SUGAR', 'PROTEINS', 'FAT', 'SALT', 'FIBER']] as double[][]

        and:
        def xReduced = ML.features.pca(X, nComponents: 2).apply(X)

        then:
        X.shape().toList() == [1014, 6]

        and:
        xReduced.shape().toList() == [1014, 2]
    }

    def "polynomial features"() {
        when:
        def xPoly = ML.features.polynomialFeatures([
            [0, 1],
            [2, 3],
            [4, 5]
        ])

        then:
        xPoly == [
            [1, 0, 1, 0, 0, 1],
            [1, 2, 3, 4, 6, 9],
            [1, 4, 5, 16, 20, 25]
        ] as double[][]

        when:
        def xPoly2 = ML.features.polynomialFeatures([
            [0, 1, 2],
            [3, 4, 5],
            [6, 7, 8]
        ])

        then:
        xPoly2 == [
            [1, 0, 1, 2, 0, 0, 0, 1, 2, 4],
            [1, 3, 4, 5, 9, 12, 15, 16, 20, 25],
            [1, 6, 7, 8, 36, 42, 48, 49, 56, 64]
        ] as double[][]
    }
}
