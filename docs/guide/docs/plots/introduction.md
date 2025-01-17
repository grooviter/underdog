## Introduction

### Echarts DSL

Underdog plots uses the [Apache Echarts](https://echarts.apache.org) under the hood, so the idea is to try to be able to render whatever is possible in Echarts. To accomplish that this project creates a Groovy DSL mimicking the Echarts Option object. You can access the DSL when customizing the chart.

At the moment the support of the Echarts Option object is limited but we aim to improve that overtime.

### Basic properties
**For every chart** we must provide methods containing the following properties:

- data entry as list of numbers
- data entry as Series
- chart title
- chart subtitle

When **the method is receiving data as List** instances:

- X coordinate label (by default is X)
- Y coordinate label (by default is Y)

In **methods receiving Series objects** the name of the X and Y coordinate will be taken from the Series' name passed as parameter.

There could be extra properties added to specific charts depending on how practical these properties are when dealing which that type of charts.

On top of that, every plot has a `customize(Closure)` which allows to customize the chart following Echarts documentation using a Groovy DSL.

### Customizing chart

All default methods provide a limited setup of the chart via the mandatory attributes we saw previously.To access the full Groovy Echarts DSL we can always access the **Options#customize(Closure)** method rendering the chart calling the **Options#show()**.

### Return Options

All plotting methods return a `memento.plots.charts.Options` instance which represents the Echarts Options object.