package underdog.graphs

import groovy.transform.EqualsAndHashCode
import groovy.transform.ToString
import groovy.transform.TupleConstructor

@ToString(includePackage = false,ignoreNulls = true)
@TupleConstructor
@EqualsAndHashCode
class Person {
    String name
    Integer age
    Object getAt(Integer index) {
        return this.properties.values().indexed()[index]
    }
}
