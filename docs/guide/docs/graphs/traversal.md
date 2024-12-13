## Traversal

To traverse a graph you can directly access the vertices or edges sets and use normal Groovy/Java mechanisms. In the following example we are looking in all vertices to find all vertices having the number two as a neighbor

```groovy title="collections (vertices)"
--8<-- "src/test/groovy/underdog/guide/graphs/TraversalSpec.groovy:collections_vertices"
```

```shell title="output"
1
```

Now in the next example we are exploring boss-employee relationships and we'd like to find all the bosses names:

```groovy title="collections (edges)" 
--8<-- "src/test/groovy/underdog/guide/graphs/TraversalSpec.groovy:collections_edges"
```

Sometimes the graph could be bigger and more complex and we could benefit from using breadthFirst (\*) or depthFirst (**) algorithms:

```groovy title="depthFirst"
--8<-- "src/test/groovy/underdog/guide/graphs/TraversalSpec.groovy:depth_first"
```