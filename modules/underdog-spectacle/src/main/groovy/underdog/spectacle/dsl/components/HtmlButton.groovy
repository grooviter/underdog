package underdog.spectacle.dsl.components

import underdog.spectacle.dsl.HasOnClick
import underdog.spectacle.dsl.HtmlElement
import underdog.spectacle.dsl.HtmlEvent

class HtmlButton extends HtmlElement implements HasOnClick {
    String text
    String icon

    HtmlEvent getOnClick() {
        return this.listEvents().find {it.name == 'click' }
    }
}
