package memento.plots.charts

import com.github.grooviter.underdog.graphs.edges.RelationshipEdge
import com.github.grooviter.underdog.plots.Options
import groovy.transform.NamedParam
import groovy.transform.NamedVariant

/**
 * Shows a Graph composed by nodes & edges
 *
 * @since 0.1.0
 */
class Graph extends Chart {

    /**
     * Node information
     *
     * @since 0.1.0
     */
    static class Node implements ToMapAware {
        String id
        String name
        Number symbolSize
        Number x, y
        String category
    }

    /**
     * Edge information
     *
     * @since 0.1.0
     */
    static class Edge implements ToMapAware {
        String source
        String target
        String value
    }

    @NamedVariant
    Options graph(
        org.jgrapht.Graph<?, RelationshipEdge> graph,
        @NamedParam(required = false, value='showEdgeLabel') boolean showEdgeLabel = false,
        @NamedParam(required = false, value='title') String chartTitle = '',
        @NamedParam(required = false, value='subtitle') String chartSubtitle = '') {
        return this.graph(
            graph.vertices.collect(Graph::toChartNode) as List<Node>,
            graph.edges.collect { toChartEdge(graph, it, showEdgeLabel) } as List<Edge>,
            chartTitle,
            chartSubtitle,
            graph.type.directed)
    }

    private static Node toChartNode(Object node) {
        if (node instanceof ToMapAware) {
            return node.toMap().subMap('id', 'name', 'symbolSize', 'x', 'y', 'category') as Node
        }
        return new Node(name: node.toString())
    }

    private static boolean isToMapAware(Class... classes) {
        return classes.every { it instanceof ToMapAware }
    }

    private static Edge toChartEdge(org.jgrapht.Graph graph, RelationshipEdge edge, boolean showLabel) {
        def target = graph.getEdgeTarget(edge)
        def source = graph.getEdgeSource(edge)

        if (isToMapAware(target.class, source.class)) {
            return [
                source: source.id ?: source.name,
                target: target.id ?: target.name,
                value: showLabel ? (edge.relation ?: edge.weight) : ""
            ]
        }

        return [
            source: source.toString(),
            target: target.toString(),
            value: showLabel ? (edge.relation ?: edge.weight) : ""
        ]
    }

    /**
     * @param nodes
     * @param edges
     * @param chartTitle
     * @param isDirected
     * @return
     * @since 0.1.0
     */
    @NamedVariant
    Options graph(
        List<Node> nodes,
        List<Edge> edges,
        @NamedParam(required = false, value='title') String chartTitle = '',
        @NamedParam(required = false, value='subtitle') String chartSubtitle = '',
        @NamedParam(required = false) boolean isDirected = false) {
        return this.graph(nodes as Node[], edges as Edge[], chartTitle, chartSubtitle, isDirected)
    }

    /**
     * @param nodes
     * @param edges
     * @param chartTitle
     * @param isDirected
     * @return
     * @since 0.1.0
     */
    @NamedVariant
    Options graph(
        Node[] nodes,
        Edge[] edges,
        @NamedParam(required = false, value='title') String chartTitle = '',
        @NamedParam(required = false, value='subtitle') String chartSubtitle = '',
        @NamedParam(required = false) boolean isDirected = false) {
        return createGridOptions(chartTitle, chartSubtitle) +
                this.graph(toMapArray(nodes), toMapArray(edges), chartTitle, chartSubtitle, isDirected)
    }

    private static Map[] toMapArray(ToMapAware[] toMapAwareList) {
        return toMapAwareList.collect { it.toMap() } as Map[]
    }

    /**
     * @param nodes
     * @param edges
     * @param chartTitle
     * @param isDirected
     * @return
     * @since 0.1.0
     */
    @NamedVariant
    Options graph(
        Map[] nodes,
        Map[] edges,
        @NamedParam(required = false, value='title') String chartTitle = '',
        @NamedParam(required = false, value='subtitle') String chartSubtitle = '',
        @NamedParam(required = false) boolean isDirected = false) {
        return createGridOptions(chartTitle, chartSubtitle) + Options.create {
            series {
                type("graph")
                layout('force')
                symbolSize(50)
                force {
                    repulsion(1000)
                    edgeLength(100)
                }
                label {
                    show(true)
                }
                if (isDirected) {
                    edgeSymbol(['circle', 'arrow'])
                }
                data(nodes.toList())
                links(edges.collect(Graph::convertEdgeValue))
            }
        }
    }

    private static Map convertEdgeValue(Map edge) {
        if (edge.value) {
            edge += [label: [formatter: edge.value, show: true ]]
        }
        return edge
    }
}
