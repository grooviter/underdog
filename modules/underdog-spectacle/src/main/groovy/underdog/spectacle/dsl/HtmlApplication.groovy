package underdog.spectacle.dsl

import groovy.transform.NamedParam
import groovy.transform.NamedVariant
import groovy.transform.TupleConstructor

/**
 * Represents a Spectacle application
 *
 * @since 0.1.0
 */
@TupleConstructor(includes = ['configuration'])
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
     * Creates a new {@link HtmlPage}
     *
     * @param path url path where the page will be accessible
     * @param theme pages html theme ('system' by default)
     * @param name logical name
     * @param closure DSL for the content of that page
     * @since 0.1.0
     */
    @NamedVariant
    void page(
        String path,
        @NamedParam(required = false) String theme = 'system',
        @NamedParam(required = false) String name = Utils.generateRandomName(),
        @NamedParam(required = false) Boolean markAsDefault = false,
        @DelegatesTo(HtmlPage) Closure closure
    ) {
        HtmlPage page = new HtmlPage(
            application: this,
            path: path,
            name: name,
            theme: theme
        ).tap { with(closure) }

        if (markAsDefault || this.pageList.isEmpty()) {
            this.defaultPage = page
        }

        this.pageList.add(page)
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
     * Finds the first {@link HtmlElement} identified by a specific name
     *
     * @param name the name of the {@link HtmlElement}
     * @return the instance of {@link HtmlElement} or null if no element has been found
     * @since 0.1.0
     */
    HtmlElement findHtmlElementByName(String name) {
        return this.elementList.find { it.name == name }
    }

    /**
     * Finds the first {@link HtmlElementWithValue} identified by a specific name
     *
     * @param name the name of the element
     * @return an instance of {@link HtmlElementWithValue} or null if no element is found
     * @since 0.1.0
     */
    HtmlElementWithValue findHtmlElementWithValueByName(String name) {
        return this.elementList
            .<HtmlElementWithValue>findAll { it instanceof HtmlElementWithValue }
            .<HtmlElementWithValue>find { it.name == name }
    }

    String getDefaultPath() {
        return this.defaultPage.path
    }
}