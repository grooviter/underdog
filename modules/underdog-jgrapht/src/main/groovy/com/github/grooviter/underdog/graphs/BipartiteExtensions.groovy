package com.github.grooviter.underdog.graphs

import com.github.grooviter.underdog.graphs.edges.RelationshipEdge
import org.jgrapht.Graph
import org.jgrapht.alg.partition.BipartitePartitioning
import org.jgrapht.graph.builder.GraphTypeBuilder

/**
 * Functions to evaluate and deal with bipartite graphs
 *
 * @since 0.1.0
 */
class BipartiteExtensions {

    /**
     * Check whether a graph is bipartite or not
     *
     * @param graph graph to evaluate
     * @return true if graph is bipartite, false otherwise
     * @since 0.1.0
     */
    static <V, E, G extends Graph<V,E>> boolean isBipartite(G graph) {
        return new BipartitePartitioning<>(graph).isBipartite()
    }

    /**
     * Returns the projected graph of a list of vertices
     *
     * @param graph the graph to get the projected graph from
     * @param vertices the vertices
     * @return a {@link Graph} of the edges part of the projection
     * @since 0.1.0
     */
    static <V, E, G extends Graph<V,E>> G projectedGraph(G graph, List<V> vertices) {
        return createProjectedGraph(graph, vertices, false)
    }

    /**
     * Returns the projected graph of a list of vertices with their weights
     *
     * @param graph the graph to get the projected graph from
     * @param vertices the vertices
     * @return a {@link Graph} of the edges part of the projection with their weight
     * @since 0.1.0
     */
    static <V, E, G extends Graph<V,E>> G weightedProjectedGraph(G graph, List<V> vertices) {
        return createProjectedGraph(graph, vertices, true)
    }

    private static <V, E, G extends Graph<V,E>> G createProjectedGraph(G graph, List<V> vertices, boolean includeWeights) {
        assert graph.isBipartite(), "projectedGraph() only supports bipartite graphs"

        def commonPairs = extractPairsWithCommonOppositeVertices(graph, vertices)
        def projectedEdges = commonPairs.collectMany {[it.value, it.value]
            .combinations() { V a, V b -> [a, b] as Set<V> }
            .<Set<V>>findAll { it.size() > 1 } as Set<Set<V>>
        }

        def projectedGraph = GraphTypeBuilder
            .forGraphType(graph.type)
            .vertexClass(vertices[0].class as Class<V>)
            .edgeClass(RelationshipEdge as Class<E>)
            .buildGraph()

        if (includeWeights) {
            projectedEdges.each(addEdgeWithGraphAndWeights(projectedGraph, extractProjectionWeights(commonPairs)))
        } else {
            projectedEdges.each(addEdgeWithGraphAndWeights(projectedGraph))
        }

        return projectedGraph as G
    }

    private static <V, E> Closure<Void> addEdgeWithGraphAndWeights(
        Graph<V, E> graph,
        Map<Set<V>, Double> weights = null) {
        return { Set<V> edgeVertices ->
            def (V a, V b) = edgeVertices
            graph.addVertex(a)
            graph.addVertex(b)
            E edge = graph.addEdge(a, b)

            if (weights && edge) {
                graph.setEdgeWeight(edge, weights[edgeVertices])
            }
        } as Closure<Void>
    }

    private static <V> Map<Set<V>, Double> extractProjectionWeights(Map<V, Set<V>> commonPairs) {
        return commonPairs
                .values()
                .inject([:] as Map<Set<V>, Double>) { Map<Set<V>, Double> agg, Set<V> next ->
                    ([next.toList()] * 2)
                        .combinations()
                        .<List<V>, Set<V>> collect { List<V> xs -> xs.toSet() }
                        .findAll { Set<V> xs -> xs.size() > 1 }
                        .each {
                            if (agg[it] == null) {
                                agg[it] = 1d
                            } else {
                                agg[it] += 1d
                            }
                        }
                    agg
                }.collectEntries { k, v -> [(k): (v % 2 == 0 ? v / 2 : v)]}
    }

    private static <V, E, G extends Graph<V,E>> Map<V, Set<V>> extractPairsWithCommonOppositeVertices(G graph, List<V> vertices) {
        def (left, right) = new BipartitePartitioning<>(graph).partitioning.partitions
        def isInLeft = vertices.every { it in left }
        def isInRight = vertices.every { it in right }

        assert isInLeft || isInRight, "vertices should belong to one of the partitions"

        return (isInLeft ? left : right)
                .findAll { it in vertices }
                .inject([:] as Map<V, Set<V>>) { Map<V, Set<V>> agg, V vertex ->
                    graph.edgesOf(vertex).each { edge ->
                        def (source, target) = graph.verticesOf(edge)
                        def key = isInLeft ? target : source
                        def val = isInLeft ? source : target
                        if (!agg[key]) {
                            agg[key] = [] as Set<V>
                        }
                        agg[key] += [val] as Set<V>
                    }
                    return agg
                }.findAll { it.value.size() > 1 }
    }
}
