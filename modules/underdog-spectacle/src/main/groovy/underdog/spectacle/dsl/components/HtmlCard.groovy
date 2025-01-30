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
        ).tap { this.addChild(it) }
    }

    HtmlCardHeader cardHeader(@DelegatesTo(HtmlCardHeader) Closure closure) {
        return new HtmlCardHeader(application: this.application, parent: this)
            .tap { this.addChild(it) }
            .tap { with(closure) }
    }

    HtmlCardBody cardBody(@DelegatesTo(HtmlContainer) Closure closure) {
        return new HtmlCardBody(application: this.application, parent: this)
            .tap { this.addChild(it) }
            .tap { with(closure) }
    }

    HtmlCardFooter cardFooter(@DelegatesTo(HtmlContainer) Closure closure) {
        return new HtmlCardFooter(application: this.application, parent: this)
            .tap { this.addChild(it) }
            .tap { with(closure) }
    }
}
