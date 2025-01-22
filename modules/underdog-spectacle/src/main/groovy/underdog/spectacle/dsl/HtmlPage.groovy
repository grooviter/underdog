package underdog.spectacle.dsl

import groovy.transform.NamedParam
import groovy.transform.NamedVariant

/**
 * Represents a new HTML page
 *
 * @since 0.1.0
 */
class HtmlPage extends HtmlContainer {

    /**
     * The URL path where the page will be accessible
     *
     * @since 0.1.0
     */
    String path

    /**
     * Represents the HTML page title
     *
     * @since 0.1.0
     */
    String title


    /**
     * CSS theme: light, dark, or system (default)
     *
     * @since 0.1.0
     */
    String theme

    /**
     * Creates a basic
     *
     * <b>IMPORTANT!</b> The result instance is not bind to any {@link HtmlApplication} so the result instance
     * should be bind to the application with some of the {@link HtmlApplication#page} methods
     *
     * @param path URL path of the page
     * @param application {@link HtmlApplication} this page will be bound to
     * @param theme dark/light theme
     * @param dsl nested elements
     * @return an instance of {@link HtmlPage}
     * @since 0.1.0
     **/
    @NamedVariant
    static HtmlPage create(
        @NamedParam(required = true) String path,
        @NamedParam(required = true) HtmlApplication application,
        @NamedParam(required = false) String theme = "light",
        @DelegatesTo(HtmlPage) Closure dsl
    ) {
        return new HtmlPage(path: path, application: application, theme: theme).tap { with(dsl) }
    }
}
