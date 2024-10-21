package yahoofinance

import spock.lang.Specification

class SimpleExampleSpec extends Specification {
    def "proof of concept"() {
        when:
        def stock = YahooFinance.get("ASML")
        def price = stock.quote.price

        then:
        price > 0
    }
}
