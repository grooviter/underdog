package underdog.spectacle.dsl.components

import groovy.transform.NamedParam
import groovy.transform.NamedVariant
import underdog.spectacle.dsl.HtmlContainer
import underdog.spectacle.dsl.Utils

/**
 * Represents a Bootstrap card element
 *
 * @since 0.1.0
 */
class HtmlCard extends HtmlContainer {

    /**
     * Renders the card header
     *
     * @param title card header title
     * @param description card header description
     * @return an instance of {@link HtmlCardHeader}
     * @since 0.1.0
     */
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

    /**
     * Represents the card header
     *
     * @paramm closure nested elements inside the header
     * @return an instance of {@link HtmlCardHeader}
     * @since 0.1.0
     */
    HtmlCardHeader cardHeader(@DelegatesTo(HtmlCardHeader) Closure closure) {
        return new HtmlCardHeader(application: this.application, parent: this)
            .tap { this.addChild(it) }
            .tap { with(closure) }
    }

    /**
     * Represents the card body
     *
     * @param name name of the html element
     * @parm className style class name
     * @param closure nested html elements
     * @return an instance of {@link HtmlCardBody}
     * @since 0.1.0
     */
    @NamedVariant
    HtmlCardBody cardBody(
        @NamedParam(required = false) String name = Utils.generateRandomName(),
        @NamedParam(required = false) String className = "",
        @DelegatesTo(HtmlContainer) Closure closure
    ) {
        return new HtmlCardBody(
            application: this.application,
            parent: this,
            name: name,
            className: className
        )
        .tap { this.addChild(it) }
        .tap { with(closure) }
    }

    /**
     * Represents the card footer
     *
     * @param closure nested html element
     * @return an instance of {@link HtmlCardFooter}
     * @since 0.1.0
     */
    HtmlCardFooter cardFooter(@DelegatesTo(HtmlContainer) Closure closure) {
        return new HtmlCardFooter(application: this.application, parent: this)
            .tap { this.addChild(it) }
            .tap { with(closure) }
    }
}
