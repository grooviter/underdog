package underdog.spectacle.dsl.components

import underdog.spectacle.dsl.HtmlElementWithValue

class HtmlRange extends HtmlElementWithValue<Number> {
    Number min
    Number max
    Number step
    String symbol
    Boolean showMarkers
    Boolean showUpdatedValue
}
