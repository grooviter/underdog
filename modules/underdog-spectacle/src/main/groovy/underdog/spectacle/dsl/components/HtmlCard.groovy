package underdog.spectacle.dsl.components

import underdog.spectacle.dsl.HtmlContainer

class HtmlCard extends HtmlContainer {
    HtmlCardHeader cardHeader(@DelegatesTo(HtmlContainer) Closure closure) {
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
