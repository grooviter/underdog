package underdog.guide.dataframe

import spock.lang.Specification
import underdog.Underdog

class FinanceSpec extends Specification {
    def "playing"() {
        setup:
        // loading data
        def tickers = Underdog
            .df()
            .read_csv("/tmp/tickers.csv", dateFormat: "yyyy-MM-dd HH:mm:ss+00:00", allowedDuplicatedNames: true)
            .renameSeries(mapper: ['Date': 'X', 'Close': 'NVDA', 'CLose-2': 'INTC', 'Close-3': 'AAPL'])

        // normalizing all tickers
        def normalized = tickers['NVDA', 'INTC', 'AAPL'].standardScale()

        // adding dates
        normalized['X'] = tickers['X'](Date, String) {
            it.format('dd/MM/yyyy')
        }

        when:
        // showing
        def options = Underdog
            .plots()
            .lines(
                normalized,
                xLabel: 'Date',
                yLabel: 'Closing Price (Normalized)',
                title: "Comparing Tickers",
                subtitle: "Tickers: NVDA, INTC, AAPL",
                smooth: true)
            .addMarkAreaInX("15/08/2024", "30/08/2024", title: "Pandemic")
            .addMarkAreaInX("03/06/2024", "26/06/2024", title: "USA Elections")
            .customize {
                tooltip {
                    trigger('axis')
                }
                legend {
                    show(true)
                    bottom("5%")
                    right("10%")
                }
            }

        then:
        options.show()
    }
}
