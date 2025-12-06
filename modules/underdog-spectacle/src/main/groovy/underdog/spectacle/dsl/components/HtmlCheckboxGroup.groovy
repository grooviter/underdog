package underdog.spectacle.dsl.components

import groovy.transform.NamedParam
import groovy.transform.NamedVariant
import underdog.spectacle.dsl.HtmlContainer

class HtmlCheckboxGroup extends HtmlContainer {

    @NamedVariant
    HtmlCheckboxGroupOption option(
        @NamedParam(required = true) String value,
        @NamedParam(required = false) String label = "",
        @NamedParam(required = false) Boolean checked = false
    ) {
        return new HtmlCheckboxGroupOption(
            value: value,
            label: label,
            checked: checked
        ).tap { this.children.add(it) }
    }

}
