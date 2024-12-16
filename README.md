## Underdog

[![Apache 2.0](https://img.shields.io/github/license/nebula-plugins/nebula-project-plugin.svg)](http://www.apache.org/licenses/LICENSE-2.0)

### Overview

Underdog is a set of Groovy libraries for data analysis:

- `underdog-dataframe`: module for working with columnar data.
- `underdog-graphs`: module exploring graph theory problems.
- `underdog-plots`: module for creating charts.
- `underdog-ml`: module to explore machine learning problems.
- `underdog-ta`: module for financial technical analysis.

### Dependencies

Depending on the module you can import one or more dependencies. Here you have the coordinates both in Gradle:

Gradle

```groovy
implementation 'com.github.grooviter:underdog-dataframe:VERSION'
implementation 'com.github.grooviter:underdog-graphs:VERSION'
implementation 'com.github.grooviter:underdog-ml:VERSION'
implementation 'com.github.grooviter:underdog-plots:VERSION'
implementation 'com.github.grooviter:underdog-ta:VERSION'
```

And Maven:

```xml
<dependency>
    <groupId>com.github.grooviter</groupId>
    <artifactId>underdog-dataframe</artifactId>
    <version>VERSION</version>
</dependency>
<dependency>
    <groupId>com.github.grooviter</groupId>
    <artifactId>underdog-graphs</artifactId>
    <version>VERSION</version>
</dependency>
<dependency>
    <groupId>com.github.grooviter</groupId>
    <artifactId>underdog-ml</artifactId>
    <version>VERSION</version>
</dependency>
<dependency>
    <groupId>com.github.grooviter</groupId>
    <artifactId>underdog-plots</artifactId>
    <version>VERSION</version>
</dependency>
<dependency>
    <groupId>com.github.grooviter</groupId>
    <artifactId>underdog-ta</artifactId>
    <version>VERSION</version>
</dependency>
```

### Documentation

You can find Underdog's documentation at https://grooviter.github.io/underdog

### License

Underdog uses [Apache License 2.0](https://www.apache.org/licenses/LICENSE-2.0)