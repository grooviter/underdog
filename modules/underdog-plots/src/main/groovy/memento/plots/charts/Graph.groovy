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
        org.jgrapht.Graph<ToMapAware, RelationshipEdge> graph,
        @NamedParam(required = false, value='title') String chartTitle = '',
        @NamedParam(required = false, value='subtitle') String chartSubtitle = '',
        @NamedParam(required = false) boolean isDirected = false) {
        return this.graph(
            graph.vertices.collect(Graph::toChartNode) as List<Node>,
            graph.edges.collect { toChartEdge(graph, it) } as List<Edge>,
            chartTitle,
            chartSubtitle,
            isDirected)
    }

    private static Node toChartNode(ToMapAware node) {
        return node.toMap().subMap('id', 'name', 'symbolSize', 'x', 'y', 'category') as Node
    }

    private static Edge toChartEdge(org.jgrapht.Graph<ToMapAware,RelationshipEdge> graph, RelationshipEdge edge) {
        def target = graph.getEdgeTarget(edge).toMap()
        def source = graph.getEdgeSource(edge).toMap()
        return [
            source: source.id ?: source.name,
            target: target.id ?: target.name,
            value: edge.relation
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
        return this.graph(toMapArray(nodes), toMapArray(edges), chartTitle, chartSubtitle, isDirected)
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
