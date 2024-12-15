## Scatter

### Simple

```groovy title="simple"
--8<-- "src/test/groovy/underdog/guide/plots/ScatterSpec.groovy:simple"
```

![](images/scatter_simple.png#only-light){ width="60%" }
![](images/scatter_simple_dark.png#only-dark){ width="60%" }

Here's the same example but using Underdog's series for X and Y axes. Given the dataframe instance `df` and series `xs` and `ys`:

```groovy title="simple series"
--8<-- "src/test/groovy/underdog/guide/plots/ScatterSpec.groovy:simple_series"
```