## Pie

!!! info "Wikipedia"

    According to [Wikipedia](https://en.wikipedia.org/wiki/Pie_chart) a pie chart (or a circle chart) is a circular statistical graphic which is divided into slices to illustrate numerical proportion.

In a pie chart, the arc length of each slice (and consequently its central angle and area) is proportional to the quantity it represents.

### Simple

To create a minimal representation of a Pie we must provide at least a collection of the **labels** of each partition, and
another collection with the **values** of each partition:

```groovy title="building pie"
--8<-- "src/test/groovy/underdog/guide/plots/PieSpec.groovy:simple"
```

![](images/pie_simple.png#only-light){ width="50%" }
![](images/pie_simple_dark.png#only-dark){ width="50%" }

### Color mapping

In some situations the color of each partition is really meaningful. For example, it would be strange to represent a 
group of race teams and represent the team Ferrari (which historically is red) with other color than red. In order
to map the colors to each partition we can provide a map of entries of type `partitionLabel: color` 
to the parameter `colorMap`:

```groovy title="color mapping"
--8<-- "src/test/groovy/underdog/guide/plots/PieSpec.groovy:color_mapping"
```

![](images/pie_color_mapping.png#only-light){ width="50%" }
![](images/pie_color_mapping_dark.png#only-dark){ width="50%" }

### Dataframe

In order to use an Underdog's dataframe we have to make sure that the name of the series should match 
the names: `names`, `values`, `colors`. 

```groovy title="dataframe"
--8<-- "src/test/groovy/underdog/guide/plots/PieSpec.groovy:dataframe"
```

![](images/pie_dataframe.png#only-light){ width="50%" }
![](images/pie_dataframe_dark.png#only-dark){ width="50%" }

You can also use Underdog's Series following the same rules:

```groovy title="series"
--8<-- "src/test/groovy/underdog/guide/plots/PieSpec.groovy:series"
```

![](images/pie_series.png#only-light){ width="50%" }
![](images/pie_series_dark.png#only-dark){ width="50%" }