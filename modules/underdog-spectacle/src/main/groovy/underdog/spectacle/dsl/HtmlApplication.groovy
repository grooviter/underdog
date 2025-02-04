package underdog.spectacle.dsl

import groovy.transform.NamedParam
import groovy.transform.NamedVariant
import groovy.transform.TupleConstructor

import java.util.function.Function

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
     * @param markAsDefault
     * @param closure DSL for the content of that page
     * @return an isntance of {@link HtmlPage}
     * @since 0.1.0
     */
    @NamedVariant
    HtmlPage page(
        String path,
        @NamedParam(required = false) String theme = 'system',
        @NamedParam(required = false) String title = '',
        @NamedParam(required = false) String name = Utils.generateRandomName(),
        @NamedParam(required = false) Boolean markAsDefault = false,
        @DelegatesTo(HtmlPage) Closure closure
    ) {
        HtmlPage page = new HtmlPage(
            application: this,
            title: title,
            path: path,
            name: name,
            theme: theme
        )

        page.tap { with(closure) }

        if (markAsDefault) {
            this.defaultPage = page
        }

        this.pageList.add(page)

        return page
    }

    HtmlPage getDefaultPage() {
        return this.@defaultPage ?: this.pageList.find()
    }

    /**
     * Creates a new {@link HtmlPage}
     *
     * @param page an instance of {@link HtmlPage}
     * @return an instance of {@link HtmlPage}
     * @since 0.1.0
     */
    HtmlPage page(Function<HtmlApplication,HtmlPage> page) {
        return page.apply(this).tap { this.pageList.add(it) }
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
        return this.getDefaultPage().path
    }
}
