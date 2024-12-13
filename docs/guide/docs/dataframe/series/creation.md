### Creating

Series are meant to be created from collections or as a transformation from another Series.

The only way to create a **Series** from a collection is invoking the extension method `toSeries()` from a list:

```groovy title="collection extension"
--8<-- "src/test/groovy/underdog/guide/dataframe/series/CreationSpec.groovy:series_from_collection"
```

Most of the time we will be dealing with a Series creation inside the scope of a Dataframe. Sometimes as the result of the transformation of another series, sometimes because we would like to fill a series from a constant value.

Lets say we have a DataFrame with some Series:

```groovy title="sample dataframe"
--8<-- "src/test/groovy/underdog/guide/dataframe/series/CreationSpec.groovy:series_as_transformation_sample"
```

```shell title="output"
 numbers
 numbers  |
-----------
       1  |
       2  |
       3  |
       4  |
       5  |
       6  |
       7  |
       8  |
       9  |
      10  |
```

And we want to create a new series named **by_two** with the result of multiplying all numbers in the **numbers** series:

```groovy title="new series"
--8<-- "src/test/groovy/underdog/guide/dataframe/series/CreationSpec.groovy:series_as_transformation_creation"
```

```shell title="output"
       numbers
 numbers  |  by_two  |
----------------------
       1  |       2  |
       2  |       4  |
       3  |       6  |
       4  |       8  |
       5  |      10  |
       6  |      12  |
       7  |      14  |
       8  |      16  |
       9  |      18  |
      10  |      20  |
```

You can also create a new Series inside a dataframe filling all rows with the same value:

```groovy title="series from value"
--8<-- "src/test/groovy/underdog/guide/dataframe/series/CreationSpec.groovy:series_from_value_creation"
```

```shell title="output"
           numbers
 numbers  |  by_two  |  one  |
------------------------------
       1  |       2  |    1  |
       2  |       4  |    1  |
       3  |       6  |    1  |
       4  |       8  |    1  |
       5  |      10  |    1  |
       6  |      12  |    1  |
       7  |      14  |    1  |
       8  |      16  |    1  |
       9  |      18  |    1  |
      10  |      20  |    1  |
```