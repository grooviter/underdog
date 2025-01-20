package underdog.spectacle.dsl

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
}
