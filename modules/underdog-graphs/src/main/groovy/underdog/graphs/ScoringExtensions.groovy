package underdog.graphs

import org.jgrapht.Graph
import org.jgrapht.alg.scoring.ClusteringCoefficient

/**
 * @since 0.1.0
 */
class ScoringExtensions {

    /**
     * Get a vertex's local clustering coefficient
     *
     * @param graph
     * @param vertex
     * @return
     * @since 0.1.0
     */
    static <V, E, G extends Graph<V,E>> Double clusteringOf(G graph, V vertex) {
        return new ClusteringCoefficient<>(graph).getVertexScore(vertex)
    }

    /**
     * Computes the average clustering coefficient. Note: the average is 0 if the graph is empty
     *
     * @param graph
     * @param vertex
     * @return
     * @since 0.1.0
     */
    static <V, E, G extends Graph<V,E>> Double clusteringAvg(G graph) {
        return new ClusteringCoefficient<>(graph).averageClusteringCoefficient
    }

    /**
     * Computes the global clustering coefficient. The global clustering coefficient C is defined
     * as C=3×number_of_triangles/number_of_triplets
     *
     * @param graph
     * @return the global clustering coefficient
     * @since 0.1.0
     */
    static <V, E, G extends Graph<V,E>> Double clusteringGlobal(G graph) {
        return new ClusteringCoefficient<>(graph).globalClusteringCoefficient
    }
}
