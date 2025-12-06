package underdog.spectacle.dsl.components

import underdog.spectacle.dsl.HtmlElement
import underdog.spectacle.dsl.HtmlPage

/**
 * Represents a navigation bar at the top of the page. Useful when having more than one
 * page in an Spectacle application
 *
 * @since 0.1.0
 */
class HtmlNavigation extends HtmlElement {

    /**
     * Returns the list of the application pages
     *
     * @return a list of {@link HtmlPage} instances
     * @since 0.1.0
     */
    List<HtmlPage> getApplicationPageList() {
        return this.application.pageList
    }

    /**
     * Returns the {@link HtmlPage} where the pagination is installed at the moment.
     * Useful for knowing the current active page
     *
     * @return an instance of {@link HtmlPage}
     * @since 0.1.0
     */
    HtmlPage getCurrentPage() {
        return this.page
    }
}
