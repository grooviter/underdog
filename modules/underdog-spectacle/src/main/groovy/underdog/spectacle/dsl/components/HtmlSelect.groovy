package underdog.spectacle.dsl.components

import underdog.spectacle.dsl.HtmlContainer

class HtmlSelect extends HtmlContainer {

    HtmlSelectOption option(Object value, Object caption) {
        return new HtmlSelectOption(caption: caption, value: value).tap { this.children.add(it) }
    }
}
