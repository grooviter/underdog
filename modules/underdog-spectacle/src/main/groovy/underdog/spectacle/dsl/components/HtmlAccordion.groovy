package underdog.spectacle.dsl.components

import groovy.transform.NamedParam
import groovy.transform.NamedVariant
import underdog.spectacle.dsl.HtmlContainer
import underdog.spectacle.dsl.Utils

class HtmlAccordion extends HtmlContainer {

    HtmlAccordionSection section(
        String title,
        @DelegatesTo(HtmlContainer) Closure closure
    ) {
        return this.section(title, null, closure)
    }

    @NamedVariant
    HtmlAccordionSection section(
        @NamedParam(required = true) String title,
        @NamedParam(required = false) String name = Utils.generateRandomName(),
        @NamedParam(required = false) String icon,
        @DelegatesTo(HtmlContainer) Closure closure
    ) {
        return new HtmlAccordionSection(
            application: application,
            parent: this,
            name: name,
            title: title,
            icon: icon
        )
        .tap { with(closure) }
        .tap { this.children.add(it) }
    }
}
