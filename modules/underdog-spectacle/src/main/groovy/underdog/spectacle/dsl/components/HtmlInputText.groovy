package underdog.spectacle.dsl.components

import underdog.spectacle.dsl.HasEnter
import underdog.spectacle.dsl.HtmlElement
import underdog.spectacle.dsl.HtmlEvent

class HtmlInputText extends HtmlElement<String> implements HasEnter {
    String placeHolder
    String icon
    String suffix
    String prefix

    HtmlEvent getOnEnter() {
        return this.listEvents().find { it.name == 'enter' }
    }
}
