package underdog.spectacle.dsl.components

import underdog.spectacle.dsl.HtmlElementWithValue

class HtmlRange extends HtmlElementWithValue<Number> {
    HtmlRange(Number value, String label) {
        this.label = label
        this.value = value
    }
}
