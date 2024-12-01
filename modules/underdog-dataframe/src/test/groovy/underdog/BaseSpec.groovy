package underdog

import spock.lang.Shared
import spock.lang.Specification

class BaseSpec extends Specification {
    static String CSV_PATH = "src/test/resources/com/github/grooviter/underdog/food.csv"
    static Integer CSV_TOTAL_SIZE = 3197

    @Shared
    DataFrame df

    void setup() {
        df = Underdog.df().read_csv(CSV_PATH, nanValues: ["NaN"], sep: ";")
    }
}
