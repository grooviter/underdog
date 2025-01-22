package underdog.spectacle.dsl.components

import groovy.transform.NamedParam
import groovy.transform.NamedVariant
import underdog.spectacle.dsl.HtmlContainer

class HtmlCard extends HtmlContainer {
    @NamedVariant
    HtmlCardHeader cardHeader(
        @NamedParam(required = false) String title = "Title",
        @NamedParam(required = false) String description = ""
    ) {
        return new HtmlCardHeader(
            application: this.application,
            parent: this,
            title: title,
            description: description
        ).tap { this.children.add(it) }
    }

    HtmlCardHeader cardHeader(@DelegatesTo(HtmlCardHeader) Closure closure) {
        return new HtmlCardHeader(application: this.application, parent: this)
            .tap { with(closure) }
            .tap { this.children.add(it) }
    }

    HtmlCardBody cardBody(@DelegatesTo(HtmlContainer) Closure closure) {
        return new HtmlCardBody(application: this.application, parent: this)
            .tap { with(closure) }
            .tap { this.children.add(it) }
    }

    HtmlCardFooter cardFooter(@DelegatesTo(HtmlContainer) Closure closure) {
        return new HtmlCardFooter(application: this.application, parent: this)
            .tap { with(closure) }
            .tap { this.children.add(it) }
    }
}
