#### CSV

##### Reading

You can read csv files via `Underdog.df().read_csv(...)` method. Here we are importing a csv files containing tornado
incidents in the USA:

```groovy title="import csv"
--8<-- "src/test/groovy/underdog/guide/dataframe/dataframe/CsvImportExportSpec.groovy:simple_read_csv"
```

```shell title="output"
                               tornadoes_1950-2014.csv
   Date     |    Time    |  State  |  State No  |  Scale  |  Injuries  |  ... |
--------------------------------------------------------------------------------
1950-01-03  |  11:00:00  |     MO  |         1  |      3  |         3  |  ... |
1950-01-03  |  11:00:00  |     MO  |         1  |      3  |         3  |      |
1950-01-03  |  11:10:00  |     IL  |         1  |      3  |         0  |      |
1950-01-03  |  11:55:00  |     IL  |         2  |      3  |         3  |      |
1950-01-03  |  16:00:00  |     OH  |         1  |      1  |         1  |      |
1950-01-13  |  05:25:00  |     AR  |         1  |      3  |         1  |      |
1950-01-25  |  19:30:00  |     MO  |         2  |      2  |         5  |      |
1950-01-25  |  21:00:00  |     IL  |         3  |      2  |         0  |      |
1950-01-26  |  18:00:00  |     TX  |         1  |      2  |         2  |      |
1950-02-11  |  13:10:00  |     TX  |         2  |      2  |         0  |      |
       ...  |       ...  |    ...  |       ...  |    ...  |       ...  |  ... |
```

##### Separator

By default the csv reader assumes the csv file is using comma (,) as the separator character, but you can provide a custom
separator. For example the following csv file content:

```shell title="separator"
--8<-- "src/test/resources/data/dataframe/io_custom_separator.csv"
```

Can be read by using the **sep** argument:

```groovy title="custom separator"
--8<-- "src/test/groovy/underdog/guide/dataframe/dataframe/CsvImportExportSpec.groovy:custom_separator"
```

##### Duplicated names

Sometimes you can find a csv where columns are repeated, by default if you don't specify you allow repeated columns
the import process will fail. Imagine we've got the following csv:

```shell title="csv with repeated cols"
--8<-- "src/test/resources/data/dataframe/io_repeated_cols.csv"
```

To allow repeated columns you should set the `allowDuplicatedNames` flag to **true**.

```groovy title="allow repeated cols"
--8<-- "src/test/groovy/underdog/guide/dataframe/dataframe/CsvImportExportSpec.groovy:allow_repeated_cols"
```

Then all repeated names will be prefixed in order with a number to avoid collisions:

```shell title="output"
                                        io_repeated_cols.csv
 bronze |  silver |  gold |  summer_total |  bronze-2  |  silver-2  |  gold-2  |  winter_total|
-----------------------------------------------------------------------------------------------
      1 |       2 |     1 |             4 |         1  |         1  |       1  |           3  |
```

##### Missing values

If a csv file contains values which should be considered as well as missing values, we can pass this information before
reading the csv file.

```shell title="csv file with missing data"
--8<-- "src/test/resources/data/dataframe/io_custom_missing_data.csv"
```

Here we're considering missing data the values **N/C** and **NONE**:

```groovy title="considering missing data"
--8<-- "src/test/groovy/underdog/guide/dataframe/dataframe/CsvImportExportSpec.groovy:custom_missing_data"
```

That will inform the reader to consider cells containing that value as missing values:

```shell title="output"
io_custom_missing_data.csv
  from   |    to    |  id   |
-----------------------------
         |  Madrid  |  123  |
 Madrid  |   Paris  |  124  |
  Paris  |  London  |  125  |
 London  |          |  126  |
```

##### Date format

If your csv files have a custom date format you can provide the date pattern as a parameter. Here we have a file
with a custom format:

```shell title="custom date format"
--8<-- "src/test/resources/data/dataframe/io_custom_date_format.csv"
```

Passing the pattern as parameter:

```groovy title="custom date format"
--8<-- "src/test/groovy/underdog/guide/dataframe/dataframe/CsvImportExportSpec.groovy:custom_date_format"
```

Gives the following output:

```shell title="output"
      io_custom_date_format.csv
   Date     |        Close         |
-------------------------------------
2014-12-05  |  0.5267500281333923  |
2014-12-08  |  0.5199999809265137  |
2014-12-09  |  0.5182499885559082  |
```

##### Skip rows/footer

If you're sure that there is data you'd like to avoid parsing, like nonsense data, you can skip parsing those rows. Check the following example:

```shell title="csv file with comments" linenums="1"
--8<-- "src/test/resources/data/dataframe/io_skipping_rows.csv"
```

There are lines we don't want to consider when creating our dataframe:

- comments in the beginning of the file (lines 1-3)
- comments in the end of the file (line 15)
- rows we don't want to parse because they don't add any meaningful information (4-8 and 11-14)

To avoid parsing any of these lines we can instruct the csv reader to skip lines in the header and/or in the
footer of the file:

```groovy title="skipping rows"
--8<-- "src/test/groovy/underdog/guide/dataframe/dataframe/CsvImportExportSpec.groovy:skipping_rows"
```


```shell title="output"
io_skipping_rows.csv
 city   |  id  |
-----------------
Madrid  |   1  |
 Paris  |   2  |
```

##### Max chars x col

You can instruct the csv reader to avoid parsing columns with more than a number of characters.


```groovy title="limiting col chars"
--8<-- "src/test/groovy/underdog/guide/dataframe/dataframe/CsvImportExportSpec.groovy:col_char_limit"
```

!!! warning

    If a column exceeds the number of characters the process will throw an exception

##### Max cols

You can instruct the csv reader to avoid parsing more than a given number of columns.

```groovy title="limiting number of cols"
--8<-- "src/test/groovy/underdog/guide/dataframe/dataframe/CsvImportExportSpec.groovy:col_limit"
```

!!! warning

    If the number of columns exceeds the number specified the process will throw an exception

