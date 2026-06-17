package underdog.spectacle.dsl
/**
 * Represents a navigation bar at the top of the page. Useful when having more than one
 * page in an Spectacle application
 *
 * @since 0.1.0
 */
class HtmlNavigation extends HtmlElement {

    /**
     * Pages can be grouped in {@link HtmlNavigationGroup} instances
     *
     * @since 0.1.0
     */
    Map<String, HtmlNavigationGroup> groups = [:]

    /**
     * The navigation top menu can point to sub menus or to pages
     *
     * @since 0.1.0
     */
    List<HtmlElement> topElements = []

    /**
     * Adds which elements are shown as top menus in the navigation. It could be
     * a page not belonging to any group, or a group containing several pages
     *
     * @param htmlPage the page added to the application
     * @since 0.1.0
     */
    void arrangeTopElements(HtmlPage htmlPage) {
        if (htmlPage.group) {
            HtmlNavigationGroup group = this.groups[htmlPage.group.name]
            group.addPage(htmlPage)
            if (group !in topElements) {
                this.topElements.add(group)
            }
        } else if (htmlPage !in this.topElements) {
            this.topElements.add(htmlPage)
        }
    }

    /**
     * Adds a new page aggregation with a given name
     *
     * @param name the key of the group
     * @param group and instance of type {@link HtmlNavigationGroup}
     * @since 0.1.0
     */
    void addGroup(String name, HtmlNavigationGroup group) {
        this.groups[name]= group
    }

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
