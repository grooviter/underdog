### Filtering

In a dataframe you can filter data by any of the Series the dataframe has.

#### Numbers

The following dataframe builds a theoretical increase of population in ten years:

```groovy title="numbers"
--8<-- "src/test/groovy/underdog/guide/dataframe/dataframe/FilteringSpec.groovy:numbers"
```

```shell title="output"
  population increase
years  |  population  |
------------------------
 1991  |        1000  |
 1992  |        2000  |
 1993  |        3000  |
 1994  |        4000  |
 1995  |        5000  |
 1996  |        6000  |
 1997  |        7000  |
 1998  |        8000  |
 1999  |        9000  |
 2000  |       10000  |
```

If we wanted to take the records after year 1995:

```groovy title="greater than"
--8<-- "src/test/groovy/underdog/guide/dataframe/dataframe/FilteringSpec.groovy:greaterThanNumber"
```

```shell title="output"
  population increase
years  |  population  |
------------------------
 1996  |        6000  |
 1997  |        7000  |
 1998  |        8000  |
 1999  |        9000  |
 2000  |       10000  |
```

Or getting records with population less than 4000:

```groovy title="less than"
--8<-- "src/test/groovy/underdog/guide/dataframe/dataframe/FilteringSpec.groovy:lessThanNumber"
```

```shell title="output"
population increase
years  |  population  |
------------------------
 1991  |        1000  |
 1992  |        2000  |
 1993  |        3000  |
```

#### String

Of course we can filter by strings. Follow up we've got a dataframe with some employee data:

```groovy title="employees"
--8<-- "src/test/groovy/underdog/guide/dataframe/dataframe/FilteringSpec.groovy:string"
```

```shell title="output"
               employees
 employees  |  department  |  payroll  |
----------------------------------------
       Udo  |       sales  |    10000  |
      John  |          it  |    12000  |
    Albert  |       sales  |    11000  |
     Ronda  |          it  |    13000  |
```

Getting employees from sales department:

```groovy title="sales"
--8<-- "src/test/groovy/underdog/guide/dataframe/dataframe/FilteringSpec.groovy:string_equals"
```

```shell title="output"
               employees
 employees  |  department  |  payroll  |
----------------------------------------
       Udo  |       sales  |    10000  |
    Albert  |       sales  |    11000  |
```

You can also use to filter by a list of possible choices:

```groovy title="in list"
--8<-- "src/test/groovy/underdog/guide/dataframe/dataframe/FilteringSpec.groovy:string_in"
```

```shell title="output"
               employees
 employees  |  department  |  payroll  |
----------------------------------------
       Udo  |       sales  |    10000  |
     Ronda  |          it  |    13000  |
```

You can even try by a regular expression. Lets look for employees with an **'o'** in their name:

```groovy title="regex"
--8<-- "src/test/groovy/underdog/guide/dataframe/dataframe/FilteringSpec.groovy:string_regex"
```

```shell title="output"
               employees
 employees  |  department  |  payroll  |
----------------------------------------
       Udo  |       sales  |    10000  |
      John  |          it  |    12000  |
     Ronda  |          it  |    13000  |
```

#### Dates

Of course in time series is crucial to allow searches by time frame.

```groovy title="dates"
--8<-- "src/test/groovy/underdog/guide/dataframe/dataframe/FilteringSpec.groovy:dates_df"
```

```shell title="output"
rented bicycles 2000
  dates     |  rented  |
-------------------------
2000-01-02  |      41  |
2000-01-03  |      47  |
2000-01-04  |      27  |
2000-01-05  |      95  |
2000-01-06  |      30  |
2000-01-07  |     162  |
2000-01-08  |      52  |
2000-01-09  |     197  |
2000-01-10  |     125  |
2000-01-11  |      15  |
       ...  |     ...  |
```

What if we'd like to get only those records of december 2000 ?

```groovy title="after"
--8<-- "src/test/groovy/underdog/guide/dataframe/dataframe/FilteringSpec.groovy:dates_after"
```

```shell title="output"
  rented bicycles 2000
  dates     |  rented  |
-------------------------
2000-12-01  |     104  |
2000-12-02  |     193  |
2000-12-03  |     107  |
2000-12-04  |     108  |
2000-12-05  |     193  |
2000-12-06  |     165  |
2000-12-07  |      82  |
2000-12-08  |      77  |
2000-12-09  |     176  |
2000-12-10  |     158  |
       ...  |     ...  |
2000-12-31  |     150  |
```

#### Summary

Here you have the tables with the supported operators:

Arithmetic

| Left       | Right    | Operator  | Example                 | Status  |
| ---------- | -------- | --------- |-------------------------| ------- |
| Series     | Series   | +         | ```df['a'] + df['b']``` | Yes     |
| Series     | Series   | -         | ```df['a'] - df['b]```  | Yes     |
| Series     | Object   | +         | ```df['a'] + 1```       | Yes     |
| Series     | Object   | -         | ```df['a'] - 1```       | Yes     |
| Series     | Object   | *         | ```df['a'] * 2```       | Yes     |
| Series     | Object   | /         | ```df['a'] / 2```       | Yes     |

filtering operators

|Type       | Operator | Example                  | Status |
| --------- | -------- |--------------------------|--------|
| String    | ==       | ```df['a'] == 'x'```     | Yes    |
| String    | !=       | ```df['a'] != 'x'```     | Yes    |
| String    | ==~      | ```df['a'] ==~ /.*/```   | Yes    |
| String    | in       | ```df['a'] in ['x']```   | Yes    |
| Number    | ==       | ```df['a'] == 1```       | Yes    |
| Number    | !=       | ```df['a'] != 1```       | Yes    |
| Number    | >        | ```df['a'] > 1```        | Yes    |
| Number    | >=       | ```df['a'] >= 1```       | Yes    |
| Number    | <        | ```df['a'] < 1```        | Yes    |
| Number    | <=       | ```df['a'] <= 1```       | Yes    |
| LocalDate | >        | ```df['a'] > date```     | Yes    |
| LocalDate | >=       | ```df['a'] >= date```    | Yes    |
| LocalDate | <        | ```df['a'] < date```     | Yes    |
| LocalDate | <=       | ```df['a'] <= date```    | Yes    |
