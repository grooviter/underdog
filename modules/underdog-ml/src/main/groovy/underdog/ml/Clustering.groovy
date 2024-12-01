package underdog.ml

import groovy.transform.NamedParam
import groovy.transform.NamedVariant
import smile.clustering.HierarchicalClustering
import smile.clustering.KMeans
import smile.clustering.linkage.CompleteLinkage
import smile.clustering.linkage.Linkage
import smile.clustering.linkage.SingleLinkage
import smile.clustering.linkage.WardLinkage

/**
 * Clustering analysis. Clustering is the assignment of a set of observations into subsets (called clusters) so that
 * observations in the same cluster are similar in some sense. Clustering is a method of unsupervised learning,
 * and a common technique for statistical data analysis used in many fields.
 *
 * @since 0.1.0
 */
class Clustering {

    /**
     * K-Means clustering. The algorithm partitions n observations into n clusters in which each observation belongs
     * to the cluster with the nearest mean. Although finding an exact solution to the k-means problem for arbitrary
     * input is NP-hard, the standard approach to finding an approximate solution
     * (often called Lloyd's algorithm or the k-means algorithm) is used widely and frequently finds reasonable
     * solutions quickly.
     *
     * @param X data to be cluster of type double[][]
     * @param nClusters number of clusters to create (default 2)
     * @param maxIterations
     * @param toleration
     * @return a list of labels corresponding to every entry in X
     * @since 0.1.0
     */
    @NamedVariant
    int[] kMeans(
            double[][] X,
            @NamedParam(required = false) int nClusters = 2,
            @NamedParam(required = false) int maxIterations = 100,
            @NamedParam(required = false) double toleration = 1.0E-4) {
        KMeans.fit(X, nClusters, maxIterations, toleration).y
    }

    /**
     * Agglomerative Hierarchical Clustering. Hierarchical agglomerative clustering seeks to build a hierarchy
     * of clusters in a bottom up approach: each observation starts in its own cluster, and pairs of clusters
     * are merged as one moves up the hierarchy.
     *
     * @param X data to be cluster of type double[][]
     * @param linkageType "ward", "single" or "complete" (default "ward")
     * @param nClusters number of clusters to create (default 2)
     * @return a list of labels corresponding to every entry in X
     * @since 0.1.0
     */
    @NamedVariant
    int[] agglomerative(
            double[][] X,
            @NamedParam(required = false) String linkageType = "ward",
            @NamedParam(required = false) int nClusters = 2){
        Linkage linkage = switch(linkageType) {
            case "ward" -> WardLinkage.of(X)
            case "single" -> SingleLinkage.of(X)
            case "complete" -> CompleteLinkage.of(X)
            default -> WardLinkage.of(X)
        }
        return HierarchicalClustering.fit(linkage).partition(nClusters)
    }
}
