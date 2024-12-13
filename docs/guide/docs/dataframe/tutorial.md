## Tutorial

### Loading data

Here we read a csv file of tornado data. Underdog infers the column types by sampling the data.

```groovy title="reading"
--8<-- "src/test/groovy/underdog/guide/dataframe/TornadosSpec.groovy:read_csv"
```

Note the file is addressed relative to the current working directory. You may have to change it for your code.

### Metadata

Often, the best way to start is to print the column names for reference:


```groovy title="list dataframe column names"
--8<-- "src/test/groovy/underdog/guide/dataframe/TornadosSpec.groovy:columns"
```

``` title="output"
[Date, Time, State, State No, Scale, Injuries, Fatalities, Start Lat, Start Lon, Length, Width]
```

The shape() method displays the row and column counts:

```groovy title=".shape of the dataframe"
--8<-- "src/test/groovy/underdog/guide/dataframe/TornadosSpec.groovy:shape"
```

```groovy title="shape of the dataframe"
--8<-- "src/test/groovy/underdog/guide/dataframe/TornadosSpec.groovy:shape2"
```

``` title="output"
59945 rows X 11 cols
```

structure() shows the index, name and type of each column

```groovy title="tornadoes schemas"
--8<-- "src/test/groovy/underdog/guide/dataframe/TornadosSpec.groovy:schema"
```

```shell title="output"
  Structure of tornadoes_1950-2014.csv
 Index  |  Column Name  |  Column Type  |
-----------------------------------------
     0  |         Date  |   LOCAL_DATE  |
     1  |         Time  |   LOCAL_TIME  |
     2  |        State  |       STRING  |
     3  |     State No  |      INTEGER  |
     4  |        Scale  |      INTEGER  |
     5  |     Injuries  |      INTEGER  |
     6  |   Fatalities  |      INTEGER  |
     7  |    Start Lat  |       DOUBLE  |
     8  |    Start Lon  |       DOUBLE  |
     9  |       Length  |       DOUBLE  |
    10  |        Width  |      INTEGER  |
```

Like many Tablesaw methods, structure() returns a table. You can then produce a string representation for display. For convenience, calling toString() on a table invokes print(), which produces a string representation of the table table. To display the table then, you can simply call.

```groovy 
println(tornadoes)
```

You can also perform other table operations on it. For example, the code below removes all columns whose type isn’t DOUBLE:

```groovy title="using schema to change dataframe structure"
--8<-- "src/test/groovy/underdog/guide/dataframe/TornadosSpec.groovy:schema_tune"
```

```shell title="output"
  Structure of tornadoes_1950-2014.csv
 Index  |  Column Name  |  Column Type  |
-----------------------------------------
     7  |    Start Lat  |       DOUBLE  |
     8  |    Start Lon  |       DOUBLE  |
     9  |       Length  |       DOUBLE  |
```

Of course, that also returned a table. We’ll cover selecting rows in more detail later.

### Previewing

The first(n) method returns a new table containing the first n rows.

```groovy title="getting first 3 lines"
--8<-- "src/test/groovy/underdog/guide/dataframe/TornadosSpec.groovy:first"
```

```shell title="output"
                                tornadoes_1950-2014.csv
   Date     |    Time    |  State  |  State No  |  Scale  |  Injuries  |  ... |
--------------------------------------------------------------------------------
1950-01-03  |  11:00:00  |     MO  |         1  |      3  |         3  |  ... |
1950-01-03  |  11:00:00  |     MO  |         1  |      3  |         3  |  ... |
1950-01-03  |  11:10:00  |     IL  |         1  |      3  |         0  |  ... |
```

### Transforming

Mapping operations take one or more columns as inputs and produce a new column as output. We can map arbitrary expressions onto the table, but many common operations are built in. You can, for example, calculate the difference in days, weeks, or years between the values in two date columns. The method below extracts the Month name from the date column into a new column.


```groovy title="creating a new series to dataframe"
--8<-- "src/test/groovy/underdog/guide/dataframe/TornadosSpec.groovy:mapping"
```

Now that you have a new column, you can add it to the table:

```groovy title="adding month series to tornadoes dataframe"
--8<-- "src/test/groovy/underdog/guide/dataframe/TornadosSpec.groovy:add_series"
```

Of course nothing prevents you from doing everything altogether.

You can remove columns from tables to save memory or reduce clutter:

```groovy title="remove series from dataframe"
--8<-- "src/test/groovy/underdog/guide/dataframe/TornadosSpec.groovy:remove_series"
```

### Sorting

Now lets sort the table in reverse order by the id column. The negative sign before the name indicates a descending sort.

```groovy title="sort asc"
--8<-- "src/test/groovy/underdog/guide/dataframe/TornadosSpec.groovy:sorting_1"
```

You can also sort in descending order:

```groovy title="sort desc"
--8<-- "src/test/groovy/underdog/guide/dataframe/TornadosSpec.groovy:sorting_2"
```

and even sorting by more than one field:

```groovy title="sort by n-fields"
--8<-- "src/test/groovy/underdog/guide/dataframe/TornadosSpec.groovy:sorting_3"
```

### Descriptive statistics

Descriptive statistics are calculated using the summary() method:

```groovy title="printing column insights"
--8<-- "src/test/groovy/underdog/guide/dataframe/TornadosSpec.groovy:column_describe"
```

Showing the following output:

```shell title="column describe output"
         Column: Fatalities
 Measure   |         Value         |
------------------------------------
        n  |                59945  |
      sum  |                 6802  |
     Mean  |  0.11347068145800349  |
      Min  |                    0  |
      Max  |                  158  |
    Range  |                  158  |
 Variance  |    2.901978053261765  |
 Std. Dev  |   1.7035193140266314  |
```

### Filtering

You can write your own methods to filter rows, but it’s easier to use the built-in filter classes as shown below:

```groovy title="filtering dataframe by column expressions"
--8<-- "src/test/groovy/underdog/guide/dataframe/TornadosSpec.groovy:filtering"
```

```shell title="output"
 tornadoes_1950-2014.csv
 State  |     Date     |
------------------------
    MO  |  1950-01-03  |
    IL  |  1950-01-03  |
    OH  |  1950-01-03  |
```

The last example above returns a table containing only the columns named in select() parameters,rather than all the columns in the original.
Totals and sub-totals

Column metrics can be calculated using methods like sum(), product(), mean(), max(), etc.

You can apply those methods to a table, calculating results on one column, grouped by the values in another.

```groovy 
--8<-- "src/test/groovy/underdog/guide/dataframe/TornadosSpec.groovy:summarizing"
```

This produces the following table, in which Group represents the Tornado Scale and Median the median injures for that group:

```shell title="output"
Median injuries by Tornado Scale
 Scale  |  Median [Injuries]  |
-------------------------------
    -9  |                  0  |
     0  |                  0  |
     1  |                  0  |
     2  |                  0  |
     3  |                  1  |
     4  |                 12  |
     5  |                107  |
```

### Cross Tabs

Tablesaw lets you easily produce two-dimensional cross-tabulations (“cross tabs”) of counts and proportions with row and column subtotals. Here’s a count example where we look at the interaction of tornado severity and US state:

```groovy title="crosstabs (count)"
--8<-- "src/test/groovy/underdog/guide/dataframe/TornadosSpec.groovy:crosstabs"
```

```shell title="output"
                       Crosstab Counts: State x Scale
 [labels]  |  -9  |   0    |   1   |   2   |   3   |  4   |  5   |  total  |
----------------------------------------------------------------------------
       AL  |   0  |   624  |  770  |  425  |  142  |  38  |  12  |   2011  |
       AR  |   1  |   486  |  667  |  420  |  162  |  29  |   0  |   1765  |
       AZ  |   1  |   146  |   71  |   16  |    3  |   0  |   0  |    237  |
       CA  |   1  |   271  |  117  |   23  |    2  |   0  |   0  |    414  |
       CO  |   3  |  1322  |  563  |  112  |   22  |   1  |   0  |   2023  |
       CT  |   0  |    18  |   53  |   22  |    4  |   2  |   0  |     99  |
       DC  |   0  |     2  |    0  |    0  |    0  |   0  |   0  |      2  |
       DE  |   0  |    22  |   26  |   12  |    1  |   0  |   0  |     61  |
       FL  |   2  |  1938  |  912  |  319  |   37  |   3  |   0  |   3211  |
       GA  |   0  |   413  |  700  |  309  |   74  |  11  |   0  |   1507  |
```

Putting it all together

Now that you’ve seen the pieces, we can put them together to perform a more complex data analysis. Lets say we want to know how frequently Tornadoes occur in the summer. Here’’s one way to approach that:

Let’s start by getting only those tornadoes that occurred in the summer.

```groovy title="tornadoes in the summer"
--8<-- "src/test/groovy/underdog/guide/dataframe/TornadosSpec.groovy:summer"
```

To get the frequency, we calculate the difference in days between successive tornadoes. The lag() method creates a column where every value equals the previous value (the prior row) of the source column. Then we can simply get the difference in days between the two dates. DateColumn has a method daysUntil() that does this. It returns a NumberColumn that we’ll call “delta”.

```groovy title="lag and delta dates"
--8<-- "src/test/groovy/underdog/guide/dataframe/TornadosSpec.groovy:summer_lag"
```

Now we simply calculate the mean of the delta column. Splitting on year keeps us from inadvertently including the time between the last tornado of one summer and the first tornado of the next.

```groovy title="create summary"
--8<-- "src/test/groovy/underdog/guide/dataframe/TornadosSpec.groovy:summary"
```

Printing summary gives us the answer by year.

```shell title="summary output"
                           tornadoes_1950-2014.csv summary
 Date year  |  Mean [Date lag(1) - Date[DAYS]]  |  Count [Date lag(1) - Date[DAYS]]  |
--------------------------------------------------------------------------------------
      1950  |               2.0555555555555545  |                               162  |
      1951  |               1.7488584474885829  |                               219  |
      1952  |               1.8673469387755088  |                               196  |
      1953  |                0.983870967741935  |                               372  |
      1954  |               0.8617283950617302  |                               405  |
```

To get a DOUBLE for the entire period, we can take the average of the annual means.

```groovy title="average second column"
--8<-- "src/test/groovy/underdog/guide/dataframe/TornadosSpec.groovy:mean_of_series"
```

``` title="Average days between tornadoes in the summer:"
0.5931137164104612
```

### Saving your data

To save a table, you can write it as a CSV file:

```groovy title="saving csv file"
tornadoes.write().csv("rev_tornadoes_1950-2014.csv");
```

And that’s it for the introduction. Please see the User Guide for more information.