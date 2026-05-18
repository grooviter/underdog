package underdog.spectacle.dsl.components

import underdog.spectacle.dsl.HtmlElement

class HtmlSubjectSummary extends HtmlElement<Value> {
    static class Value {
        String primaryText
        String secondaryText
        String icon
        String iconBackground
    }
}
