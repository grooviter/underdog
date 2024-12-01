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
            .rename(mapper: ['Date': 'X', 'Close': 'NVDA', 'CLose-2': 'INTC', 'Close-3': 'AAPL'])

        // normalizing all tickers
        def normalized = tickers['NVDA', 'INTC', 'AAPL'].standardScale()

        // adding dates
        normalized['X'] = tickers['X'](Date, String) {
            it.format('dd/MM/yyyy')
        }

        expect:
        // showing
        Underdog
            .plots()
            .lines(
                normalized,
                xLabel: 'Date',
                yLabel: 'Closing Price (Normalized)',
                title: "Comparing Tickers",
                subtitle: "Tickers: NVDA, INTC, AAPL", smooth: true)
            .customize {
                tooltip {
                    trigger('axis')
                }
                legend {
                    show(true)
                }
            }.show()
    }
}
