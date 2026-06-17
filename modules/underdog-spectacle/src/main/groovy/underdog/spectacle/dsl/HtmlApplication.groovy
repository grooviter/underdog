package underdog.spectacle.dsl

import groovy.transform.NamedParam
import groovy.transform.NamedVariant

import java.util.function.Function

/**
 * Represents a Spectacle application
 *
 * @since 0.1.0
 */
class HtmlApplication {
    Map<String,?> configuration

    /**
     * List of {@link HtmlPage} accessible in this application
     *
     * @since 0.1.0
     */
    List<HtmlPage> pageList = []

    /**
     * List of {@link HtmlEvent} that could be triggered in this application
     *
     * @since 0.1.0
     */
    List<HtmlEvent> eventList = []

    /**
     * All {@link HtmlElement} instances in this application
     *
     * @since 0.1.0
     */
    List<HtmlElement> elementList = []

    /**
     * List of handlers responsible for exposing static resources such as images, audios...
     *
     * @since 0.1.0
     */
    List<ResourceHandler> resourceHandlerList = []

    /**
     * Contains the ids of the elements of this application. When invoked with a non present
     * key, it will generate a new id for that key.
     *
     * @since 0.1.0
     */
    Map<String,String> field = [:].<String, String>withDefault(Utils::generateRandomName)

    /**
     * Whether the current application is in development mode or not
     *
     * @since 0.1.0
     */
    boolean development

    /**
     * Default page. When adding a new html page you can mark it as default.
     *
     * - If only one html page has been added then it becomes the default
     * - If no default page has been marked as default then the first added becomes the default
     *
     * @since 0.1.0
     */
    HtmlPage defaultPage

    /**
     * Default theme for all pages (light|dark)
     *
     * @since 0.1.0
     */
    String defaultTheme = 'light'

    /**
     * Whether to show the navigation bar or not. By default if more than one page is
     * added to the application the navigation bar it's shown
     *
     * @since 0.1.0
     */
    Boolean showNavigation = true

    /**
     * The application can have a single navigation. This navigation is shared with all pages
     * to keep coherence between pages
     *
     * @since 0.1.0
     */
    HtmlNavigation applicationNavigation = new HtmlNavigation(application: this)

    /**
     * Adds a new page group
     *
     * @param name the name of the group
     * @param title the title shown
     * @param icon the icon for the group menu
     * @return an instance of {@link HtmlNavigationGroup}
     * @since 0.1.0
     */
    @NamedVariant
    HtmlNavigationGroup group(
        String name,
        String title,
        String icon
    ) {
        HtmlNavigationGroup group = new HtmlNavigationGroup(name: name, title: title, icon: icon, application: this)
        this.applicationNavigation.addGroup(name, group)
        return group
    }

    /**
     * Whether the application should show the navigation
     *
     * @return true if the showNavigation flag is active or the number of pages is greater than 1
     * @since 0.1.0
     */
    Boolean shouldShowPagination() {
        return this.showNavigation || this.pageList.size() > 1
    }

    /**
     * Creates a new {@link HtmlPage}
     *
     * @param path url path where the page will        if (this.pageList.size() > 0) {
            this.pageList.each { it.navigation() }
        } be accessible
     * @param theme pages html theme ('system' by default)
     * @param title title of the html page
     * @param icon a bootstrap icon with class name syntax, for example: `bi bi-question`
     * @param group name of the group this page belongs to
     * @param name logical name
     * @param markAsDefault
     * @param closure DSL for the content of that page
     * @return an instance of {@link HtmlPage}
     * @since 0.1.0
     */
    @NamedVariant
    HtmlPage page(
        String path,
        @NamedParam(required = false) String theme = '',
        @NamedParam(required = false) String title = '',
        @NamedParam(required = false) String icon = '',
        @NamedParam(required = false) HtmlNavigationGroup group,
        @NamedParam(required = false) String name = Utils.generateRandomName(),
        @NamedParam(required = false) Boolean markAsDefault = false,
        @DelegatesTo(HtmlPage) Closure closure
    ) {
        HtmlPage page = new HtmlPage(
            application: this,
            title: title,
            icon: icon,
            group: group,
            path: path,
            name: name,
            theme: theme
        )

        page.tap { with(closure) }

        if (markAsDefault) {
            this.defaultPage = page
        }

        addPage(page)

        return page
    }

    /**
     * Adds a new static resources endpoint handler
     *
     * This handler not only exposed a set of static resources. It can also be used in event
     * functions to save new resources in the system.
     *
     * @param path the url path of where resources will be exposed
     * @param dir file system directory where the files will be located phisically
     * @param name name of the resource handler. Useful for accessing it
     * @param allowListing whether to allow directory listing from the endpoint or not
     * @return an instance of type {@link ResourceHandler}
     * @since 0.1.0
     */
    @NamedVariant
    ResourceHandler resources(
        @NamedParam(required = true) String path,
        @NamedParam(required = false) String dir,
        @NamedParam(required = false) String name = Utils.generateRandomName(),
        @NamedParam(required = false) Boolean allowListing = false
    ) {
        return new ResourceHandler(
            name: name,
            path: path,
            dir: dir,
            allowListing: allowListing
        ).tap {this.resourceHandlerList.add(it) }
    }

    ResourceHandler findResourceHandlerByName(String name) {
        return this.resourceHandlerList.find { it.name == name }
    }

    HtmlPage getDefaultPage() {
        return this.@defaultPage ?: this.pageList.find()
    }

    /**
     * Creates a new {@link HtmlPage}
     *
     * @param page an instance of {@link Function}
     * @return an instance of {@link HtmlPage}
     * @since 0.1.0
     */
    HtmlPage page(Function<HtmlApplication,HtmlPage> page) {
        return page.apply(this).tap(this::addPage)
    }

    /**
     * Creates a new {@link HtmlPage}
     *
     * @param group the group the page belongs to
     * @param page an instance of {@link Function}
     * @return an instance of {@link HtmlPage}
     * @since 0.1.0
     */
    HtmlPage page(HtmlNavigationGroup group, Function<HtmlApplication, HtmlPage> page) {
        return page.apply(this)
            .tap {it.group = group }
            .tap { addPage(it) }
    }

    /**
     * Adds a {@link HtmlPage} to the application and applies default values
     *
     * @param a new @{link HtmlPage}
     * @since 0.1.0
     */
    void addPage(HtmlPage htmlPage) {
        if (this.defaultTheme && !htmlPage.theme){
            htmlPage.theme = this.defaultTheme
        }

        htmlPage.application = this
        htmlPage.htmlNavigation = this.applicationNavigation

        this.applicationNavigation.arrangeTopElements(htmlPage)
        this.pageList.add(htmlPage)
    }

    /**
     * Adds a new event to this application
     *
     * @param event an instance of {@link HtmlEvent}
     * @since 0.1.0
     */
    void addEvent(HtmlEvent event) {
        this.eventList.add(event)
    }

    /**
     * Adds a new element to this application
     *
     * @param element an instance of {@link HtmlElement}
     * @since 0.1.0
     */
    void addElement(HtmlElement element) {
        this.elementList.add(element)
    }

    /**
     * Finds the first {@link HtmlElement} identified by a specific name
     *
     * @param name the name of the element
     * @return an instance of {@link HtmlElement} or null if no element is found
     * @since 0.1.0
     */
    HtmlElement findHtmlElementWithValueByName(String name) {
        return this.elementList.find { it.name == name }
    }

    String getDefaultPath() {
        return this.getDefaultPage().path
    }
}
