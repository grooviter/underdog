package underdog.spectacle.dsl.components

import underdog.spectacle.dsl.HtmlElement

class HtmlRange extends HtmlElement<Number> {
    Number min
    Number max
    Number step
    String symbol
    Boolean showMarkers
    Boolean showUpdatedValue
}
