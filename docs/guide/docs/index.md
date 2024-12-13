# Underdog

## About 
**Underdog** is a set of Groovy libraries for data analysis.

It doesn't expect to be the best data analysis tool, but it combines Groovy's expressiveness, and some of the best Java data analysis libraries to at least, make data analysis fun.

## Modules

Underdog project covers several data analysis fields with the following subprojects:

### underdog-dataframe

Combines tools for working with dataframes and series. You can use it adding the following dependency:

```groovy title="gradle"
implementation "com.github.grooviter:underdog-dataframe:VERSION"
```

More information in the [DataFrame](./dataframe/index.md) section

### underdog-graphs

It helps working on graph theory data structures and algorithms. You can use it adding the following dependency:

```groovy title="gradle"
implementation "com.github.grooviter:underdog-graphs:VERSION"
```

More information in the [Graphs](./graphs/index.md) section

### underdog-ml

Contains machine learning algorithms and evaluation mechanisms. You can use it adding the following dependency:

```groovy title="gradle"
implementation "com.github.grooviter:underdog-ml:VERSION"
```

More information in the [ML](./ml/index.md) section

### underdog-plots

Creates different types of charts using the [Apache Echarts](https://echarts.apache.org) library underneath. You can use it adding the following dependency:

```groovy title="gradle"
implementation "com.github.grooviter:underdog-plots:VERSION"
```

More information in the [Plots](./plots/index.md) section

### underdog-ta

The technical analysis module is a wrapper over the [Ta4j](https://ta4j.github.io/ta4j-wiki/) library. It adds some extension modules to the existent classes so that it makes easier to play with technical indicators and rules and **integrate with Underdog's dataframes and series**. You can use it adding the following dependency:

```groovy title="gradle"
implementation "com.github.grooviter:underdog-ta:VERSION"
```

More information in the [Technical Analysis](./ta/index.md) section