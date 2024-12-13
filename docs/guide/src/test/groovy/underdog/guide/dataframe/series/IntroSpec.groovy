package underdog.guide.dataframe.series

import spock.lang.Specification

class IntroSpec extends Specification {
    def "create series"() {
        when:
        // --8<-- [start:create_series]
        // from a range of numbers
        def numbers = (1..4).toSeries("numbers")

        // from a range of letters
        def letters = ('A'..'C').toSeries("letters")

        // from a list
        def stuff = [1, 2, null, 3, 4].toSeries("stuff")
        // --8<-- [end:create_series]

        and:
        // --8<-- [start:operations]
        // multiplying a series by a number
        def doubleSeries = numbers * 2

        // multiplying a series by another series
        def rowProduct = numbers * stuff.dropna()

        // dividing a series
        def halves = stuff / 2

        // using custom transformation to create a new series
        def custom = letters(String, String) { "letter-$it".toString() }
        // --8<-- [end:operations]

        and:
        // --8<-- [start:statistics]
        def mean = doubleSeries.mean()
        def max = doubleSeries.max()
        def min = doubleSeries.min()
        def avg = doubleSeries.avg()
        // --8<-- [end:statistics]

        then:
        custom.toList() == ['letter-A', 'letter-B', 'letter-C']
        doubleSeries.avg() == 5.0
    }
}
