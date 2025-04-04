package underdog.impl.extensions

import spock.lang.Specification

class SeriesOperatorsExtensionsSpec extends Specification {
    def "[Series/Operators] >= Number"() {
        setup:
        def data = [
            X: (1..10),
            y: (10..100).by(10)
        ].toDataFrame("plot")

        when:
        def result = data[data['X'] >= 3 & data['X'] <= 8]

        then:
        result.size() == 6
        result['X'] as List<Integer> == [3, 4, 5, 6, 7, 8]
        result['y'] as List<Integer> == [30, 40, 50, 60, 70, 80]
    }
}
