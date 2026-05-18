package underdog.spectacle.dsl

import groovy.transform.NamedParam
import groovy.transform.NamedVariant
import underdog.spectacle.dsl.components.HtmlNavigation

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
     * Represents the HTML page pre-title. Can be used for breadcrumbs
     *
     * @since 0.1.0
     */
    String preTitle

    /**
     * Accepts a bootstrap icon, for example : 'bi bi-question'
     *
     * @since 0.1.0
     */
    String icon

    /**
     * CSS theme: light, dark, or system (default)
     *
     * @since 0.1.0
     */
    String theme

    /**
     * @since 0.1.0
     */
    List<HtmlEvent> eventList = []

    /**
     * An {@link HtmlPage} can have a navigation panel in case we want to navigate through
     * the pages of the application
     *
     * @since 0.1.0
     */
    HtmlNavigation htmlNavigation

    /**
     * @param event
     * @since 0.1.0
     */
    void addEvent(HtmlEvent event){
        this.eventList.add(event)
        this.application.addEvent(event)
    }

    /**
     * Renders a navigation panel in the page. The navigation panel won't be
     * added as a children of the page like the rest of the children elements
     * as it has to be treated differently
     *
     * @return an instance of {@link HtmlNavigation}
     * @since 0.1.0
     */
    HtmlNavigation navigation() {
        return new HtmlNavigation().tap {
            page = this
            parent = this
            application = this.application
            htmlNavigation = it
        }
    }

    /**
     * List all {@link HtmlEvent} attached by a given element
     *
     * @param name the name of the {@link HtmlElement}
     * @return a list of {@link HtmlEvent} attached to a given element
     * @since 0.1.0
     */
    List<HtmlEvent> listEventsByComponentName(String name) {
        return this.eventList.findAll { it.htmlFieldName == name }
    }

    /**
     * Creates a basic
     *
     * <b>IMPORTANT!</b> The result instance is not bind to any {@link HtmlApplication} so the result instance
     * should be bind to the application with some of the {@link HtmlApplication#page} methods
     *
     * @param path URL path of the page
     * @param title page title
     * @param application {@link HtmlApplication} this page will be bound to
     * @param theme dark/light theme
     * @param dsl nested elements
     * @return an instance of {@link HtmlPage}
     * @since 0.1.0
     **/
    @NamedVariant
    static HtmlPage create(
        @NamedParam(required = true) String path,
        @NamedParam(required = false) String title,
        @NamedParam(required = false) String preTitle,
        @NamedParam(required = true) HtmlApplication application,
        @NamedParam(required = false) String theme,
        @NamedParam(required = false) String icon,
        @DelegatesTo(HtmlPage) Closure dsl
    ) {
        return new HtmlPage(
            path: path,
            title: title,
            preTitle: preTitle,
            application: application,
            theme: theme,
            icon: icon
        ).tap { with(dsl) }
    }
}
