package underdog.spectacle.dsl.components

import underdog.spectacle.dsl.HtmlElement

class HtmlProgressPanel extends HtmlElement<Value> {
    String title

    static class Value {
        String progressText
        Double value
    }
}
