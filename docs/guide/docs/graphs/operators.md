## Operators

You can apply arithmetic operations over graphs. Here for example you can apply a union operation over two graphs using the `+` operator:

```groovy title="merging graphs"
--8<-- "src/test/groovy/underdog/guide/graphs/OperatorsSpec.groovy:operators_plus"
```

You can check how the result has all the vertices from the previous merged graphs:

```shell title="output"
--8<-- "src/test/groovy/underdog/guide/graphs/OperatorsSpec.groovy:operators_plus_result"
```