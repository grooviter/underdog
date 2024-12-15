## Tutorial

### Prerequisites

#### Dependencies

The modules required to follow this tutorial are the `graphs` and `plots` modules:

```groovy title="gradle"
implementation 'com.github.grooviter:underdog-graphs:VERSION'
implementation 'com.github.grooviter:underdog-plots:VERSION'
```

or if you're using Maven:

```xml title="maven"
<dependency>
    <groupId>com.github.grooviter</groupId>
    <artifactId>underdog-graphs</artifactId>
    <version>VERSION</version>
</dependency>
<dependency>
    <groupId>com.github.grooviter</groupId>
    <artifactId>underdog-plots</artifactId>
    <version>VERSION</version>
</dependency>
```

### Creating a graph

Creating an empty graph without vertices and edges:

```groovy title="create a graph"
--8<-- "src/test/groovy/underdog/guide/graphs/TutorialSpec.groovy:import"

--8<-- "src/test/groovy/underdog/guide/graphs/TutorialSpec.groovy:create"
```

The `graph(Class)` informs what is the type of the vertices this graph is going to have. This time vertices in this
graph will be of type `String` but vertices could be potentially of any type.

### Vertices / Nodes

Lets create a graph with two nodes without any edge between them:

![adding vertices](images/tutorial_adding_vertices.png#only-light){ width="40%" }
![adding vertices](images/tutorial_adding_vertices_dark.png#only-dark){ width="40%" }

There are a couple of ways of adding more vertices to a graph. One is when creating the graph:

```groovy title="adding vertices at creation time"
--8<-- "src/test/groovy/underdog/guide/graphs/TutorialSpec.groovy:add_vertices_at_creation_time"
```

You can also add more vertices after the graph has been created:

```groovy title="adding vertices after creation"
--8<-- "src/test/groovy/underdog/guide/graphs/TutorialSpec.groovy:add_vertices_after_creation"
```

You can add simple type vertices, but you can also add more complex objects. Imagine we can add the relationships between employees in a given company. First lets define the `Employee` class:

```groovy title="Employee"
--8<-- "src/test/groovy/underdog/guide/graphs/TutorialSpec.groovy:employee"
``` 

Now we can create a Graph and add relationships between employees:

```groovy title="Adding employees"
--8<-- "src/test/groovy/underdog/guide/graphs/TutorialSpec.groovy:add_employees"
```

### Edges

Graphs normally are not very useful without setting edges between nodes. Lets add an edge between two nodes:

![adding edges](images/tutorial_adding_edges.png#only-light){ width="40%" }
![adding edges](images/tutorial_adding_edges_dark.png#only-dark){ width="40%" }

We can add vertices at creation time:

```groovy title="Adding edges at creation time"
--8<-- "src/test/groovy/underdog/guide/graphs/TutorialSpec.groovy:adding_edges_at_creation"
```

You can omit adding vertices before adding edges if all vertices are going to be included in `edge(...)` method calls. The following example produces the same graph as the previous one:

```groovy title="avoid using vertex(...)"
--8<-- "src/test/groovy/underdog/guide/graphs/TutorialSpec.groovy:adding_edges_no_need_for_vertices"
```

It's also possible to add edges after the graph has been created:

```groovy title="Adding edges after creation time"
--8<-- "src/test/groovy/underdog/guide/graphs/TutorialSpec.groovy:adding_edges_after_creation"
```

You can also **add several edges at once** using `edges(...)`

![adding edges](images/tutorial_adding_edges_II.png#only-light){ width="40%" }
![adding edges](images/tutorial_adding_edges_II_dark.png#only-dark){ width="40%" }

```groovy title="adding several edges"
--8<-- "src/test/groovy/underdog/guide/graphs/TutorialSpec.groovy:adding_several_at_once"
```

We can at any point ask about **how many vertices and edges there are** in the graph:

```groovy title="shape"
--8<-- "src/test/groovy/underdog/guide/graphs/TutorialSpec.groovy:shape"
```

which prints:

```shell title="output"
4 vertices X 3 edges
```

### Elements of a graph

- vertices
- edges
- adjacent
- degree

Lets create a graph first and then ask for its elements:

```groovy title="graph"
--8<-- "src/test/groovy/underdog/guide/graphs/TutorialSpec.groovy:elements_graph"
```

The lets ask for its vertices, edges, neighbors.

```groovy title="graph elements"
--8<-- "src/test/groovy/underdog/guide/graphs/TutorialSpec.groovy:elements"
``` 

### Removing elements

Here there are some example on how to remove vertices and edges from a given graph. When using
the operator minus (`-`) the result return the graph minus the element removed whereas the `removeXXX(,,,)` functions only return a boolean value: true if the element was successfully removed, or false
if it couldn't be removed from graph.

```groovy title="removing elements"
--8<-- "src/test/groovy/underdog/guide/graphs/TutorialSpec.groovy:removing_elements"
```

### Graph types

You can create different types of graphs. There are a couple of methods you can use:

```groovy title="graph types"
--8<-- "src/test/groovy/underdog/guide/graphs/TutorialSpec.groovy:graph_types"
```

!!! tip

    Because graphs in underdog are using [JGraphT](https://github.com/jgrapht/jgrapht) underneath you can always create an instance of any type of graph 
    directly using JGraphT api.

### What to use as vertices

You can use almost anything as a vertex. The only mandatory condition is that **the graph must be able to distinguish between vertices**. For that your vertex should implement both equals and hashcode methods. In Groovy you can use the `@Canonical` annotation to get that.

### Analyzing graphs

The structure of the graph can be analyzed by using various functions.

```groovy title="graph"
--8<-- "src/test/groovy/underdog/guide/graphs/TutorialSpec.groovy:analyzing_graph"
```

What is the shape of the graph ?

```groovy title="shape"
--8<-- "src/test/groovy/underdog/guide/graphs/TutorialSpec.groovy:analyzing_graph_shape"
```

What is the clustering of the graph ?

```groovy title="clustering of the graph"
--8<-- "src/test/groovy/underdog/guide/graphs/TutorialSpec.groovy:analyzing_graph_clustering_global"
```

What is the clustering avg ?

```groovy title="clustering average"
--8<-- "src/test/groovy/underdog/guide/graphs/TutorialSpec.groovy:analyzing_graph_clustering_avg"
```

And what about the clustering of a given vertex ?

```groovy title="clustering of a vertex"
--8<-- "src/test/groovy/underdog/guide/graphs/TutorialSpec.groovy:analyzing_graph_clustering_vertex"
```

What is the vertex with max degree ?

```groovy title="max degree"
--8<-- "src/test/groovy/underdog/guide/graphs/TutorialSpec.groovy:analyzing_graph_max_degree"
```

Lets say we want to sort vertices by degree in descending order:

```groovy title="sort vertices by degree (desc)"
--8<-- "src/test/groovy/underdog/guide/graphs/TutorialSpec.groovy:analyzing_graph_sort_by_degree"
```