# Underdog

## About 
**Underdog** is a set of Groovy libraries for data analysis.

It doesn't expect to be the best data analysis tool, but it combines Groovy's expressiveness, and some of the best Java data analysis libraries to at least, make data analysis fun.

## Modules

Underdog project covers several data analysis fields with the following subprojects:

### :material-table:{ .lg .middle } underdog-dataframe

Combines tools for working with dataframes and series. You can use it adding the dependency:

=== "Gradle"
    ```groovy
    implementation "com.github.grooviter:underdog-dataframe:VERSION"
    ```
=== "Maven"
    ```xml
    <dependency>
        <groupId>com.github.grooviter</groupId>
        <artifactId>underdog-dataframe</artifactId>
        <version>VERSION</version>
    </dependency>
    ```
=== "Grapes"
    ```groovy
    @Grab("com.github.grooviter:underdog-dataframe:VERSION")
    ```

More information in the [DataFrame](./dataframe/index.md) section

### :material-graph:{ .lg .middle } underdog-graphs

It helps working on graph theory data structures and algorithms. You can use it adding the following dependency:

=== "Gradle"
    ```groovy
    implementation "com.github.grooviter:underdog-graphs:VERSION"
    ```
=== "Maven"
    ```xml
    <dependency>
        <groupId>com.github.grooviter</groupId>
        <artifactId>underdog-graphs</artifactId>
        <version>VERSION</version>
    </dependency>
    ```
=== "Grapes"
    ```groovy
    @Grab("com.github.grooviter:underdog-graphs:VERSION")
    ```

More information in the [Graphs](./graphs/index.md) section

### :material-brain:{ .lg .middle } underdog-ml

Contains machine learning algorithms and evaluation mechanisms. You can use it adding the following dependency:

=== "Gradle"
    ```groovy
    implementation "com.github.grooviter:underdog-ml:VERSION"
    ```
=== "Maven"
    ```xml
    <dependency>
        <groupId>com.github.grooviter</groupId>
        <artifactId>underdog-ml</artifactId>
        <version>VERSION</version>
    </dependency>
    ```
=== "Grapes"
    ```groovy
    @Grab("com.github.grooviter:underdog-ml:VERSION")
    ```

More information in the [ML](./ml/index.md) section

### :material-chart-bar:{ .lg .middle } underdog-plots

Creates different types of charts using the [Apache Echarts](https://echarts.apache.org) library underneath. You can use it adding the following dependency:

=== "Gradle"
    ```groovy
    implementation "com.github.grooviter:underdog-plots:VERSION"
    ```
=== "Maven"
    ```xml
    <dependency>
        <groupId>com.github.grooviter</groupId>
        <artifactId>underdog-plots</artifactId>
        <version>VERSION</version>
    </dependency>
    ```
=== "Grapes"
    ```groovy
    @Grab("com.github.grooviter:underdog-plots:VERSION")
    ```

More information in the [Plots](./plots/index.md) section

### :material-chart-line:{ .lg .middle } underdog-ta

The technical analysis module is a wrapper over the [Ta4j](https://ta4j.github.io/ta4j-wiki/) library. It adds some extension modules to the existent classes so that it makes easier to play with technical indicators and rules and **integrate with Underdog's dataframes and series**. You can use it adding the following dependency:

=== "Gradle"
    ```groovy
    implementation "com.github.grooviter:underdog-ta:VERSION"
    ```
=== "Maven"
    ```xml
    <dependency>
        <groupId>com.github.grooviter</groupId>
        <artifactId>underdog-ta</artifactId>
        <version>VERSION</version>
    </dependency>
    ```
=== "Grapes"
    ```groovy
    @Grab("com.github.grooviter:underdog-ta:VERSION")
    ```

More information in the [Technical Analysis](./ta/index.md) section