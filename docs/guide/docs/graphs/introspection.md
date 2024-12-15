## Introspection

You can ask the tree for different attributes such as the degrees:

```groovy title="max degree"
--8<-- "src/test/groovy/underdog/guide/graphs/IntrospectionSpec.groovy:max_degree"
```

Imagine a more complex example where our nodes are beans. Here we have a class representing a person:

```groovy title="Person"
--8<-- "src/test/groovy/underdog/guide/graphs/IntrospectionSpec.groovy:person"
```

This class implements the method `getAt(int)` which allows to extract the object attribute values via 
Groovy destructuring. That would become handy later on.

!!! note

    You can learn more about Groovy destructuring in the [Groovy docs](https://docs.groovy-lang.org/latest/html/documentation/#_object_destructuring_with_multiple_assignment)

We create the graph:

```groovy title="Graph using complex objects"
--8<-- "src/test/groovy/underdog/guide/graphs/IntrospectionSpec.groovy:person_graph"
```

Then look for the person with more relationship and extract that person name and age:

```groovy title="Person with more relationships"
--8<-- "src/test/groovy/underdog/guide/graphs/IntrospectionSpec.groovy:person_max_degree"
```

Do you remember we implemented destructuring for the Person class ? Here the `maxDegree()` function returns an instance of type Person, therefore we can access the properties name and age using destructuring knowing that the property in index 0 is the name and the property in index 1 is age.