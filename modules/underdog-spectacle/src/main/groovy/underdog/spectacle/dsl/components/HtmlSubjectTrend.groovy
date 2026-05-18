package underdog.spectacle.dsl.components

import underdog.spectacle.dsl.HtmlElement

class HtmlSubjectTrend extends HtmlElement<Value> {
    String symbol
    String deltaSymbol

    static class Value {
        Number value
        Number delta
        String text
    }
}
