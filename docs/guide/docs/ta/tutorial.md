## Tutorial

!!! info
    
    This getting started section mimics the [getting started](https://ta4j.github.io/ta4j-wiki/Getting-started.html) section steps from [Ta4j wiki](https://ta4j.github.io/ta4j-wiki) but using underdog-ta module. You can compare both entries to see the differences.

### Prerequisites

#### Dependencies

To be able to follow the tutorial you should add the following modules to your gradle project:

```groovy title="gradle"
implementation 'com.github.grooviter:underdog-ta:VERSION'
implementation 'com.github.grooviter:underdog-plots:VERSION'
```

or in maven:

```xml title="maven"
<dependency>
    <groupId>com.github.grooviter</groupId>
    <artifactId>underdog-ta</artifactId>
    <version>VERSION</version>
</dependency>
<dependency>
    <groupId>com.github.grooviter</groupId>
    <artifactId>underdog-plots</artifactId>
    <version>VERSION</version>
</dependency>
```

#### Data

TODO

### BarSeries vs DataFrame

It's important to start defining some concepts.

- **BarSeries**: Ta4j's BarSeries is like a dataframe containing series (columns) such as open price, lower price, close price, and volume data.

- **DataFrame**: Underdog's dataframe which is composed of different Series (columns), Each Series or column is like an array of objects.

As this module integrates Ta4j with Underdog there will be methods which converts from BarSeries to DataFrame and vice versa.

### Loading data

In this example we are using Underdog to load stock quotes from a csv file and create a DataFrame:

```groovy title="stock quotes from csv"
--8<-- "src/test/groovy/underdog/guide/ta/GettingStartedSpec.groovy:loading_data"
```

!!! warning

    If dates are not treated as dates the csv reader will consider them as strings. That will cause you problems when converting an Underdog's DataFrame to Ta4j's BarSeries.

Which outputs something like the following (the prices and volume are truncated here to make it look good).

```shell title="output"
                                stock_quotes_10_years.csv
           Date             |  Adj Close  |  Close |  High  |  Low  |  Open  |   Volume |
-----------------------------------------------------------------------------------------
 2014-12-05 00:00:00+00:00  |   0.50      |  0.52  |  0.52  |  0.52 |  0.52  |  165680  |
```

In order to successfully convert the dataframe to a bar series the name of the series (columns) should match to the expected ones which are: DATE, CLOSE, HIGH, LOW, OPEN, VOLUME.

```groovy title="renaming"
--8<-- "src/test/groovy/underdog/guide/ta/GettingStartedSpec.groovy:renaming_cols"
```


```shell title="output"
                          stock_quotes_10_years.csv
           DATE             |  CLOSE |  HIGH  |  LOW  |  OPEN  |  VOLUME  |
---------------------------------------------------------------------------
 2014-12-05 00:00:00+00:00  |  0.52  |  0.52  |  0.52 |  0.52  |  165680  |
```

### DataFrame to BarSeries

Because we need to convert the dataframe to a BarSeries in order to create technical analysis rules:

```groovy title="to bar series"
--8<-- "src/test/groovy/underdog/guide/ta/GettingStartedSpec.groovy:convert_to_bar_series"
```

Now we can operate with the bar series.

### Indicators

Now we can start creating some indicators and metrics. This time we are getting metrics based on the closing price indicator:

```groovy title="indicators"
--8<-- "src/test/groovy/underdog/guide/ta/GettingStartedSpec.groovy:building_indicators"
```

```groovy title="output"
5-bars-SMA value at the 42nd index: 0.503899997472763
```

### Building a trading strategy

Now that we've got a couple of indicators ready lets build a strategy. Strategies are made of two trading rules: one for entry (buying), the other for exit (selling).

```groovy title="strategy base"
--8<-- "src/test/groovy/underdog/guide/ta/GettingStartedSpec.groovy:strategy_base"
```

We can use Groovy's syntax to refactor the rules a little bit:

```groovy title="strategy using operators"
--8<-- "src/test/groovy/underdog/guide/ta/GettingStartedSpec.groovy:strategy_or"
```

In Groovy **|** and **&** represent calls to methods **or** and **and** of any object (any object having those methods implemented). So if your object has these methods, you can substitute your method call by the operators.

### Backtesting

What is backtesting ? According to Investopedia:

"Backtesting is the general method for seeing how well a strategy or model would have done after the fact. It assesses the viability of a trading strategy by discovering how it would play out using historical data. If backtesting works, traders and analysts may have the confidence to employ it going forward"
-- Investopedia, https://www.investopedia.com/terms/b/backtesting.asp

The backtest step is pretty simple:

```groovy title="backtesting"
--8<-- "src/test/groovy/underdog/guide/ta/GettingStartedSpec.groovy:backtesting_base"
```

```groovy title="output"
Number of positions (trades) for our strategy: 57
```

For many scenarios we can run a base strategy backtesting by just executing the **run** method in the BarSeries object and pass directly the entry (buying) rule and the exit (selling) rule:

```groovy title="backtesting"
--8<-- "src/test/groovy/underdog/guide/ta/GettingStartedSpec.groovy:backtesting_extension"
```

We can see this visually. Follow-up showing only trades from 2024-04-01:

```groovy title="plotting trades"
--8<-- "src/test/groovy/underdog/guide/ta/GettingStartedSpec.groovy:show_trades"
```

![](images/getting_started_trades.png#only-light){ width="60%" }
![](images/getting_started_trades_dark.png#only-dark){ width="60%" }

We can also visualize winning vs losing positions.

```groovy title="winning vs losing"
--8<-- "src/test/groovy/underdog/guide/ta/GettingStartedSpec.groovy:show_positions"
```

![](images/getting_started_winners.png#only-light){ width="60%" }
![](images/getting_started_winners_dark.png#only-dark){ width="60%" }

### Analyzing our results

Here is how we can analyze the results of our backtest:

```groovy title="analysis"
--8<-- "src/test/groovy/underdog/guide/ta/GettingStartedSpec.groovy:analysis_base"
```

```shell title="output"
Winning positions ratio: 0.54385964912280701754385964912281
Return over Max Drawdown: 3.4519153297405649777237484258582
Our return vs buy-and-hold return: 0.0040904249296843023287166775087440
```

Showing these metrics in a chart:

```groovy title="radar"
--8<-- "src/test/groovy/underdog/guide/ta/GettingStartedSpec.groovy:analysis_radar"
```

Which displays:

![](images/getting_started_radar.png#only-light){ width="60%" }
![](images/getting_started_radar_dark.png#only-dark){ width="60%" }
