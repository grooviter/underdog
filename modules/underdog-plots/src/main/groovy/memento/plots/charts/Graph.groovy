package memento.plots.charts

import com.github.grooviter.underdog.graphs.edges.RelationshipEdge
import com.github.grooviter.underdog.plots.Options
import groovy.transform.EqualsAndHashCode
import groovy.transform.NamedParam
import groovy.transform.NamedVariant
import groovy.transform.ToString
import org.jgrapht.GraphPath

/**
 * A graph is made up of vertices (also called nodes or points) which are connected by edges
 * (also called arcs, links or lines)
 *
 * @link https://en.wikipedia.org/wiki/Graph_theory
 * @since 0.1.0
 */
class Graph extends Chart {

    private static final List<String> EDGE_COLORS = [
        '#c23531',
        '#2f4554',
        '#61a0a8',
        '#d48265',
        '#91c7ae',
        '#749f83',
        '#ca8622',
        '#bda29a',
        '#6e7074',
        '#546570',
        '#c4ccd3'
    ]

    /**
     * Node information
     *
     * @since 0.1.0
     */
    @ToString(includes=['id', 'name'])
    @EqualsAndHashCode(includes=['id', 'name'])
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
    @ToString(includes=['source', 'target'])
    @EqualsAndHashCode(includes = ['source', 'target'])
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
        /**
         * Color string in case we want to highlight some path
         */
        String color
        /**
         * Width of the edge line
         */
        String width
    }

    /**
     * Path information
     *
     * @since 0.1.0
     */
    static class Path implements ToMapAware {
        /**
         * Edges which belong to the path
         */
        List<Edge> edges
        /**
         * Name of the path
         */
        String name
        /**
         * Color of the path
         */
        String color
        /**
         * Width of the path
         */
        String width
    }

    /**
     * Renders a graph
     *
     * @param graph an instanceof of {@link Graph}
     * @param chartTitle title of the chart
     * @param chartSubtitle subtitle of the chart
     * @param showEdgeLabel whether or not showing edge labels
     * @param chartSymbolSize size of the node symbol
     * @param paths {@link GraphPath} instances used to highlight a path in the graph
     * @return an instance of {@link Options}
     * @since 0.1.0
     */
    @NamedVariant
    Options graph(
        org.jgrapht.Graph<?, RelationshipEdge> graph,
        @NamedParam(required = false, value='title') String chartTitle = '',
        @NamedParam(required = false, value='subtitle') String chartSubtitle = '',
        @NamedParam(required = false, value='showEdgeLabel') boolean showEdgeLabel = false,
        @NamedParam(required = false, value='symbolSize') Number chartSymbolSize = 50,
        @NamedParam(required = false) List<GraphPath> paths = []) {
        return this.graph(
            graph.vertices.collect { toChartNode(it) },
            graph.edges.collect { toChartEdge(graph, it) },
            chartTitle,
            chartSubtitle,
            showEdgeLabel,
            chartSymbolSize,
            graph.type.directed,
            toPaths(graph, paths))
    }

    /**
     * Renders a graph
     *
     * @param nodes list of {@link Node}
     * @param edges list of {@link Edge}
     * @param chartTitle title of the chart
     * @param chartSubtitle subtitle of the chart
     * @param showEdgeLabel whether or not showing edge labels
     * @param chartSymbolSize size of the node symbol
     * @param isDirected whether to show the graph with directed edges or not
     * @param paths {@link Path} instances used to highlight a path in the graph
     * @return an instance of {@link Options}
     * @since 0.1.0
     */
    @NamedVariant
    Options graph(
        List<Node> nodes,
        List<Edge> edges,
        @NamedParam(required = false, value='title') String chartTitle = '',
        @NamedParam(required = false, value='subtitle') String chartSubtitle = '',
        @NamedParam(required = false, value='showEdgeLabel') boolean showEdgeLabel = false,
        @NamedParam(required = false, value='symbolSize') Number chartSymbolSize = 50,
        @NamedParam(required = false) boolean isDirected = false,
        @NamedParam(required = false) List<Path> paths = []) {
        List<Map> nodeList = nodes.collect { it.toMap() }
        List<Map> edgeList = edges
            .collect { applyPathListToEdgeMap(paths, it) }
            .collect { it.toMap() }
            .collect { setChartEdgeValueAndLineStyle(it, showEdgeLabel) }
        return createGridOptions(chartTitle, chartSubtitle) +
            Options.create {
                series {
                    type("graph")
                    layout('force')
                    symbolSize(chartSymbolSize)
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
                    data(nodeList)
                    links(edgeList)
                }
            }
    }

    private static Edge applyPathListToEdgeMap(List<Path> pathList, Edge edge) {
        Path path = pathList.find { edge in it.edges }

        if (!path) {
            return edge
        }

        return edge.tap {
            it.color = path.color
            it.width = path.width
        }
    }

    private static List<Path> toPaths(org.jgrapht.Graph<?, RelationshipEdge> graph, List<GraphPath> paths) {
        List<Path> resultPathList = []
        paths.eachWithIndex { GraphPath entry, int i ->
            def path = new Path(
                name: "path-$i",
                color: EDGE_COLORS[i] ?: '#000000',
                width: 5,
                edges: entry.edgeList.collect { toChartEdge(graph, it as RelationshipEdge) })
            resultPathList << path
        }
        return resultPathList
    }

    private static Map setChartEdgeValueAndLineStyle(Map edge, boolean showEdgeLabel) {
        if (edge.value && showEdgeLabel) {
            edge += [
                label: [
                    formatter: "${edge.value}",
                    show: true
                ],
            ]
        }

        if (edge.color) {
            edge += [
                lineStyle: [
                    color: edge.color,
                    width: edge.width
                ]
            ]
        }

        return edge
    }

    private static Node toChartNode(Object node) {
        if (node instanceof ToMapAware) {
            return node.toMap().subMap('id', 'name', 'symbolSize', 'x', 'y', 'category') as Node
        }
        return new Node(name: node.toString())
    }

    private static Edge toChartEdge(org.jgrapht.Graph graph, RelationshipEdge edge) {
        def target = graph.getEdgeTarget(edge)
        def source = graph.getEdgeSource(edge)

        if (source instanceof ToMapAware && target instanceof ToMapAware) {
            Map sourceMap = source.toMap()
            Map targetMap = target.toMap()
            return [
                source: sourceMap.id ?: sourceMap.name,
                target: targetMap.id ?: targetMap.name,
                value: edge.relation ?: edge.weight
            ]
        }

        return [
            source: source.toString(),
            target: target.toString(),
            value: edge.relation ?: edge.weight
        ]
    }
}
