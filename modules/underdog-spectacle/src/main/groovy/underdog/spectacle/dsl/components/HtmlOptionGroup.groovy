package underdog.spectacle.dsl.components

import underdog.spectacle.dsl.HtmlContainer

class HtmlOptionGroup extends HtmlContainer {
    HtmlOptionGroupOption option(
        String value,
        Boolean checked = false
    ) {
        return new HtmlOptionGroupOption(
            name: this.name,
            value: value,
            checked: checked
        ).tap { this.children.add(it) }
    }
}
