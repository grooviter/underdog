package underdog.guide.plots

import underdog.Underdog
import spock.lang.Specification

class ExtensionsSpec extends Specification {
    def "dataframe"() {
        expect:
        // --8<-- [start:dataframe]
        Underdog.df()
            // Dataframe created
            .from(X: 10..<20, Y: [1, 3, 9, 3, 19, 10, 11, 4, 14, 20], "dataframe name")
            // Plots extensions for dataframe add plots methods such as `scatter()`
            .scatter()
            .show()
        // --8<-- [end:dataframe]
    }
}
