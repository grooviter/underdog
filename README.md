## Underdog

[![Apache 2.0](https://img.shields.io/github/license/nebula-plugins/nebula-project-plugin.svg)](http://www.apache.org/licenses/LICENSE-2.0)

### Overview

Underdog is a set of Groovy libraries for data analysis:

- `underdog-dataframe`: combines tools for working with columnar data
- `underdog-graphs`: can be used for exploring graph theory problems.
- `underdog-plots`: renders different types of charts and adds integration with rest of Underdog's modules
- `underdog-ml`: can be used to explore machine learning problems.
- `underdog-ta`: cab be used for financial technical analysis.

### Dependencies

Depending on the module you can import one or more dependencies. Here you have the coordinates both in Gradle and Maven:

Gradle

```groovy
implementation 'com.github.grooviter:underdog-dataframe:VERSION'
implementation 'com.github.grooviter:underdog-graphs:VERSION'
implementation 'com.github.grooviter:underdog-ml:VERSION'
implementation 'com.github.grooviter:underdog-plots:VERSION'
implementation 'com.github.grooviter:underdog-ta:VERSION'
```
Maven

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