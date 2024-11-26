package memento.plots.charts

import com.github.grooviter.underdog.graphs.edges.RelationshipEdge
import com.github.grooviter.underdog.plots.Options
import groovy.transform.NamedParam
import groovy.transform.NamedVariant

/**
 * A graph is made up of vertices (also called nodes or points) which are connected by edges
 * (also called arcs, links or lines)
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

        /**
         * Node id
         */
        String id
        /**
         * Node name
         */
        String name
        /**
         * Node symbol size
         */
        Number symbolSize
        /**
         * Where to locate the node in the x axis
         */
        Number x
        /**
         * Where to locate the node in the y axis
         */
        Number y
        /**
         * To which category the node belongs to
         */
        String category
    }

    /**
     * Edge information
     *
     * @since 0.1.0
     */
    static class Edge implements ToMapAware {

        /**
         * Source node name or id
         */
        String source
        /**
         * Target node name or id
         */
        String target
        /**
         * Value of the edge label
         */
        String value
    }

    /**
     * Renders a graph
     *
     * @param graph an instanceof of {@link Graph}
     * @param closure extra options for customizing the chart
     * @param chartTitle title of the chart
     * @param chartSubtitle subtitle of the chart
     * @param showEdgeLabel whether or not showing edge labels
     * @return an instance of {@link Options}
     * @since 0.1.0
     */
    @NamedVariant
    Options graph(
        org.jgrapht.Graph<?, RelationshipEdge> graph,
        @DelegatesTo(Options) Closure closure,
        @NamedParam(required = false, value='title') String chartTitle = '',
        @NamedParam(required = false, value='subtitle') String chartSubtitle = '',
        @NamedParam(required = false, value='showEdgeLabel') boolean showEdgeLabel = false) {
        return Options.create(closure) + this.graph(graph, chartTitle, chartSubtitle, showEdgeLabel)
    }

    /**
     * Renders a graph
     *
     * @param graph an instanceof of {@link Graph}
     * @param chartTitle title of the chart
     * @param chartSubtitle subtitle of the chart
     * @param showEdgeLabel whether or not showing edge labels
     * @return an instance of {@link Options}
     * @since 0.1.0
     */
    @NamedVariant
    Options graph(
        org.jgrapht.Graph<?, RelationshipEdge> graph,
        @NamedParam(required = false, value='title') String chartTitle = '',
        @NamedParam(required = false, value='subtitle') String chartSubtitle = '',
        @NamedParam(required = false, value='showEdgeLabel') boolean showEdgeLabel = false) {
        return this.graph(
            graph.vertices.collect(Graph::toChartNode) as List<Node>,
            graph.edges.collect { toChartEdge(graph, it, showEdgeLabel) } as List<Edge>,
            title: chartTitle,
            subtitle: chartSubtitle,
            isDirected: graph.type.directed)
    }

    /**
     * Renders a graph
     *
     * @param nodes list of {@link Node}
     * @param edges list of {@link Edge}
     * @param closure extra options for customizing the chart
     * @param chartTitle title of the chart
     * @param chartSubtitle subtitle of the chart
     * @param isDirected whether to show the graph with directed edges or not
     * @return an instance of {@link Options}
     * @since 0.1.0
     */
    @NamedVariant
    Options graph(
            List<Node> nodes,
            List<Edge> edges,
            @DelegatesTo(Options) Closure closure,
            @NamedParam(required = false, value='title') String chartTitle = '',
            @NamedParam(required = false, value='subtitle') String chartSubtitle = '',
            @NamedParam(required = false) boolean isDirected = false) {
        return Options.create(closure) + this.graph(nodes, edges, chartTitle, chartSubtitle, isDirected)
    }

    /**
     * Renders a graph
     *
     * @param nodes list of {@link Node}
     * @param edges list of {@link Edge}
     * @param chartTitle title of the chart
     * @param chartSubtitle subtitle of the chart
     * @param isDirected whether to show the graph with directed edges or not
     * @return an instance of {@link Options}
     * @since 0.1.0
     */
    @NamedVariant
    Options graph(
        List<Node> nodes,
        List<Edge> edges,
        @NamedParam(required = false, value='title') String chartTitle = '',
        @NamedParam(required = false, value='subtitle') String chartSubtitle = '',
        @NamedParam(required = false) boolean isDirected = false) {
        return this.graph(toMapArray(nodes), toMapArray(edges), chartTitle, chartSubtitle, isDirected)
    }

    /**
     * Renders a graph
     *
     * @param nodes list of {@link Map} properties matching the {@link Node} properties
     * @param edges list of {@link Map} properties matching the {@link Edge} properties
     * @param closure extra options for customizing the chart
     * @param chartTitle title of the chart
     * @param chartSubtitle subtitle of the chart
     * @param isDirected whether to show the graph with directed edges or not
     * @return an instance of {@link Options}
     * @since 0.1.0
     */
    @NamedVariant
    Options graph(
        Map[] nodes,
        Map[] edges,
        @DelegatesTo(Options) Closure closure,
        @NamedParam(required = false, value='title') String chartTitle = '',
        @NamedParam(required = false, value='subtitle') String chartSubtitle = '',
        @NamedParam(required = false) boolean isDirected = false) {
        return Options.create(closure) + this.graph(nodes, edges, chartTitle, chartSubtitle, isDirected)
    }

    /**
     * Renders a graph
     *
     * @param nodes list of {@link Map} properties matching the {@link Node} properties
     * @param edges list of {@link Map} properties matching the {@link Edge} properties
     * @param chartTitle title of the chart
     * @param chartSubtitle subtitle of the chart
     * @param isDirected whether to show the graph with directed edges or not
     * @return an instance of {@link Options}
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

    private static Map[] toMapArray(List<ToMapAware> toMapAwareList) {
        return toMapAwareList.collect { it.toMap() } as Map[]
    }

    private static Map convertEdgeValue(Map edge) {
        if (edge.value) {
            edge += [label: [formatter: edge.value, show: true ]]
        }
        return edge
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
}
