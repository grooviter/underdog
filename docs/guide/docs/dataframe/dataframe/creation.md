### Creation

The easiest way to create a Dataframe is using the Underdog extension method `Underdog.df()`. Here we're creating an empty DataFrame:

```groovy title="empty dataframe"
--8<-- "src/test/groovy/underdog/guide/dataframe/dataframe/CreationSpec.groovy:empty"
```

We can create a dataframe with a series of map entries representing series. In this case the key entry is the name of the series and the value is a collection which will become the content of the series.

```groovy title="dataframe from a map"
--8<-- "src/test/groovy/underdog/guide/dataframe/dataframe/CreationSpec.groovy:from_map"
```

```shell title="output"
people-dataframe
 name   |  age  |
------------------
  John  |   22  |
 Laura  |   34  |
Ursula  |   83  |
```

Underdog dataframe library adds additional methods to collection types so that you can convert from collections to Dataframes. And example is invoking the `toDataFrame(...)` method from the map directly:

```groovy title="map extension"
--8<-- "src/test/groovy/underdog/guide/dataframe/dataframe/CreationSpec.groovy:map_extension"
```

You can also pass a list of maps to the `Underdog.df().from(col, name)` method:

```groovy title="collection of maps"
--8<-- "src/test/groovy/underdog/guide/dataframe/dataframe/CreationSpec.groovy:collectionsOfMaps"
```

```shell title="output"
people-dataframe
 name   |  age  |
------------------
  John  |   22  |
 Laura  |   34  |
Ursula  |   83  |
```

Here there is also an extension method for collections so that, **IF** your list complies to this structure you can call to the method `toDataFrame(name)` and create a DataFrame from that collection.

```groovy title="collection extension"
--8<-- "src/test/groovy/underdog/guide/dataframe/dataframe/CreationSpec.groovy:collectionsOfMaps_extension"
```
