package com.github.grooviter.underdog.smile


class ClusteringSpec extends BaseSpec {
    def "k-means clustering"() {
        setup:
        def df = loadFoodDataFrame()

        and:
        def X = df[['CARBS', 'SUGAR', 'PROTEINS', 'FAT', 'SALT', 'FIBER']] as double[][]

        when: "normalizing and clustering values"
        def xNormalized = Smile.features.minMaxScaler(X).apply(X)
        def (xNormalizedLength, _) = xNormalized.shape()
        def y = Smile.clustering.kMeans(xNormalized, nClusters: 4)

        then: "labels size should be equals to the source data array"
        y.size() == xNormalizedLength

        and: "cluster types should equal to 4"
        y.toList().unique().size() == 4
    }

    def "Agglomerative clustering"(){
        setup: "getting data"
        def df = loadFoodDataFrame()

        and: "getting the interesting features to cluster"
        def X = df[['CARBS', 'SUGAR', 'PROTEINS', 'FAT', 'SALT', 'FIBER']] as double[][]

        when: "clustering those features with a given linkage type and a set of clusters"
        def y = Smile.clustering.agglomerative(X, linkageType: "ward", nClusters: 5)

        then: "clustering type array should match the X length"
        y.size() == X.length

        and: "the different types should be equal to the nCluster param"
        y.toList().unique().size() == 5
    }
}
