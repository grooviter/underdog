package underdog.ml.xvalidation

import groovy.transform.TupleConstructor
import org.codehaus.groovy.runtime.DefaultGroovyMethods

@TupleConstructor
final class BinaryClassificationReport {
    Map<String, Number> positive
    Map<String, Number> negative
    Map<String, Number> totals
    List targetNames

    @Override
    String toString() {
        Object[] values = [positive, negative, totals]
            .collect {it.values().toList() }
            .sum()

        return """\
                 accuracy    precision    recall    f1-score    support
    1              %.2f        %.2f        %.2f       %.2f        %d
not_1              %.2f        %.2f        %.2f       %.2f        %d
avg/totals         %.2f        %.2f        %.2f       %.2f        %d
        """.formatted(values)
    }

    Object asType(Class clazz) {
        if (clazz instanceof Map) {
            return [
                (targetNames?.last() ?: '1'): positive,
                (targetNames?.first() ?: 'not_1'): negative,
                'avg_totals': totals
            ]
        }
        return DefaultGroovyMethods.asType(this, clazz)
    }
}
