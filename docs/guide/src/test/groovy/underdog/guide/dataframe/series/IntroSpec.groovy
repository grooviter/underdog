package underdog.guide.dataframe.series

import spock.lang.Specification

class IntroSpec extends Specification {
    def "create series"() {
        when:
        // tag::create_series[]
        def numbers = (1..4).toSeries("numbers") // <1>
        def letters = ('A'..'C').toSeries("letters") // <2>
        def stuff = [1, 2, null, 3, 4].toSeries("stuff") // <3>
        // end::create_series[]

        and:
        // tag::operations[]
        def doubleSeries = numbers * 2 // <1>
        def rowProduct = numbers * stuff.dropna() // <2>
        def halves = stuff / 2 // <3>
        def custom = letters(String, String) { "letter-$it" } // <4>
        // end::operations[]

        and:
        // tag::statistics[]
        def mean = doubleSeries // <1>
        def max = doubleSeries.max() // <2>
        def min = doubleSeries.min()
        def avg = doubleSeries.average()
        // end::statistics[]

        then:
        custom.toList() == ['leter-A', 'letter-B', 'letter-C']
    }
}
