package com.github.grooviter.underdog.graphs

import com.github.grooviter.underdog.graphs.imports.Imports
import org.jgrapht.Graph

/**
 * @since 0.1.0
 */
class ImportExtensions {

    /**
     * @param graph
     * @return
     * @since 0.1.0
     */
    static <V, E, G extends Graph<V,E>> Imports<V, E, G> importFrom(G graph) {
        return new Imports<>(graph)
    }
}
