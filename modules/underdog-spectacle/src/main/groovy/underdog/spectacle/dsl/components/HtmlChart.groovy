package underdog.spectacle.dsl.components

import underdog.plots.Options
import underdog.spectacle.dsl.HtmlElement

class HtmlChart extends HtmlElement {
    Closure<Options> supplier

    String getChartAsString() {
        if (this.value) {
            return this.supplier.call(this.value).toJson()
        }
        return this.supplier.call().toJson()
    }
}
