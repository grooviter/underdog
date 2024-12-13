### Intro

A **Series** object represents a named one-dimensional array. It also supports operations and statistical methods. It also has operations to deal with missing values. You can create a **Series** object from different sources:

```groovy title="create"
--8<-- "src/test/groovy/underdog/guide/dataframe/series/IntroSpec.groovy:create_series"
```

You can use operator symbols to apply simple operations over the Series object:

```groovy title="operations"
--8<-- "src/test/groovy/underdog/guide/dataframe/series/IntroSpec.groovy:operations"
```

Sometimes you may want to analyze a given Series object by using statistical methods:

```groovy title="statistics"
--8<-- "src/test/groovy/underdog/guide/dataframe/series/IntroSpec.groovy:statistics"
```

You can find all statistical available methods in the `SeriesStatsExtensions` class.
