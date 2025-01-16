package underdog.spectacle.dsl.components

import underdog.plots.Options
import underdog.spectacle.dsl.HtmlElementWithValue

class HtmlChart extends HtmlElementWithValue {
    Closure<Options> supplier

    String getChartAsString() {
        if (this.value) {
            return this.supplier.call(this.value).toJson()
        }
        return ""
    }
}
