## Graphs

### Simple

Here's a simple graph representation just showing 2 edges connecting 3 vertices. We only have to pass an instance
of a graph to the `graph(...)` function:

![](images/graphs_simple.png#only-light){ width="40%" }
![](images/graphs_simple_dark.png#only-dark){ width="40%" }

```groovy title="simple graph"
--8<-- "src/test/groovy/underdog/guide/plots/GraphSpec.groovy:simple"
```

### Directed graph

If the graph we're passing to the `plots().graph(...)` function is a directed graph, the direction of the edges will
show up:

![](images/graphs_directed.png#only-light){ width="40%" }
![](images/graphs_directed_dark.png#only-dark){ width="40%" }

```groovy title="directed graph"
--8<-- "src/test/groovy/underdog/guide/plots/GraphSpec.groovy:directed"
```

### Edge labels

If we want to show the edges labels we can do so by setting the `showEdgeLabel` parameter to `true`:

![](images/graphs_edge_labels.png#only-light){ width="40%" }
![](images/graphs_edge_labels_dark.png#only-dark){ width="40%" }

```groovy title="edge labels"
--8<-- "src/test/groovy/underdog/guide/plots/GraphSpec.groovy:show_labels"
```

### Showing paths

Sometimes we may want highlight a given path between vertices. We can use the parameter `paths` which receives a list
of paths to highlight any number of paths:

![](images/graphs_paths.png#only-light){ width="40%" }
![](images/graphs_paths_dark.png#only-dark){ width="40%" }

```groovy title="show paths"
--8<-- "src/test/groovy/underdog/guide/plots/GraphSpec.groovy:graph_path"
```

### Graph domain

To go one step further and play with vertices sizes and colors, we can use the graph chart domain classes. These classes are:

- `underdog.plots.charts.Graph.Node`: represents a node graphically (size, color, label)
- `underdog.plots.charts.Graph.Edge`: represents an edge graphically (width, color, label)

![](images/graphs_domain.png#only-light){ width="40%" }
![](images/graphs_domain_dark.png#only-dark){ width="40%" }

```groovy title="domain classes"
--8<-- "src/test/groovy/underdog/guide/plots/GraphSpec.groovy:graph_domain"
```

### Customize

As any of the charts in Underdog, we can use the `customize(...)` method of any chart to customize the chart by using
the Groovy Echarts DSL:

```groovy title="customization"
--8<-- "src/test/groovy/underdog/guide/plots/GraphSpec.groovy:customize"
```