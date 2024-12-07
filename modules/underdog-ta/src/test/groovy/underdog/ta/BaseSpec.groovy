package underdog.ta

import org.ta4j.core.BarSeries
import spock.lang.Specification
import underdog.DataFrame
import underdog.Underdog

class BaseSpec extends Specification {
    private static String TICKERS_PATH = "src/test/resources/nvidia.csv"
    private static String TICKERS_DATE_FORMAT = "yyyy-MM-dd HH:mm:ss+00:00"
    private static String SP500_PATH = "src/test/resources/sp500.csv"
    private static String SP500_DATE_FORMAT = "MM/dd/yyyy"
    private static String VIX_PATH = "src/test/resources/vix.csv"
    private static String VIX_DATE_FORMAT = "MM/dd/yyyy"

    BarSeries createBarSeries() {
        return createDataFrame()
                .loc[__, ['Date', 'Open', 'High', 'Low', 'Close', 'Volume']]
                .toBarSeries()
    }

    DataFrame getSP500Data() {
        return Underdog.df().read_csv(SP500_PATH, dateFormat: SP500_DATE_FORMAT)
    }

    DataFrame getVixData() {
        return Underdog.df().read_csv(VIX_PATH, dateFormat: VIX_DATE_FORMAT)
    }

    DataFrame createDataFrame() {
        return Underdog.df().read_csv(TICKERS_PATH, dateFormat: TICKERS_DATE_FORMAT, allowedDuplicatedNames: true)
    }
}
