package underdog.spectacle.dsl.components

import underdog.spectacle.dsl.HtmlContainer

class HtmlSelect extends HtmlContainer {

    HtmlSelectOption option(Object key, Object value) {
        return new HtmlSelectOption(key: key, value: value).tap { this.children.add(it) }
    }
}
