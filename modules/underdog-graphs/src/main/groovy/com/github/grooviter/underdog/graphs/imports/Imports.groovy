package com.github.grooviter.underdog.graphs.imports

import groovy.transform.NamedParam
import groovy.transform.NamedVariant
import groovy.transform.TupleConstructor
import groovy.transform.stc.ClosureParams
import groovy.transform.stc.SimpleType
import org.jgrapht.Graph
import org.jgrapht.nio.csv.CSVFormat
import org.jgrapht.nio.csv.CSVImporter

import static org.jgrapht.nio.csv.CSVFormat.EDGE_LIST

/**
 * @since 0.1.0
 */
@TupleConstructor
class Imports<V, E, G extends Graph<V,E>> {
    private static final Closure<String> IDENTITY_STRING = { String x -> x}

    /**
     * @since 0.1.0
     */
    G graph


    /**
     * @param filePath
     * @param sep
     * @return
     * @since 0.1.0
     */
    @NamedVariant
    G adjacencyList(
            String filePath,
            @NamedParam(required = false) String sep = ' ') {
        return adjacencyList(filePath, sep, IDENTITY_STRING as Closure<V>)
    }

    /**
     * @param filePath
     * @param sep
     * @param vertexSupplier
     * @return
     * @since 0.1.0
     */
    @NamedVariant
    G adjacencyList(
            String filePath,
            @NamedParam(required = false) String sep = ' ',
            @ClosureParams(value = SimpleType, options = ['java.lang.String']) Closure<V> vertexSupplier) {
        new CSVImporter<>(CSVFormat.ADJACENCY_LIST, sep.toCharacter()).with {
            setVertexFactory(vertexSupplier)
            importGraph(graph, new File(filePath))
        }
        return graph
    }

    /**
     * @param filePath
     * @param hasWeight
     * @param sep
     * @return
     * @since 0.1.0
     */
    @NamedVariant
    G edgeList(
            String filePath,
            @NamedParam(required = false) boolean hasWeight = false,
            @NamedParam(required = false) String sep = ' ') {
        return edgeList(filePath, hasWeight, sep, IDENTITY_STRING as Closure<V>)
    }

    /**
     * @param filePath
     * @param hasWeight
     * @param sep
     * @param vertexSupplier
     * @since 0.1.0
     */
    @NamedVariant
    G edgeList(
            String filePath,
            @NamedParam(required = false) boolean hasWeight = false,
            @NamedParam(required = false) String sep = ' ',
            @ClosureParams(value = SimpleType, options = ['java.lang.String']) Closure<V> vertexSupplier) {
        new CSVImporter<>(EDGE_LIST, sep.toCharacter()).tap {
            if (hasWeight) {
                setParameter(CSVFormat.Parameter.EDGE_WEIGHTS, true)
            }
            setVertexFactory(vertexSupplier)
            importGraph(graph, new File(filePath))
        }
        return graph
    }
}
