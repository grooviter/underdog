package underdog.spectacle.dsl

/**
 * Represents an HTML element
 *
 * @since 0.1.0
 */
abstract class HtmlElement {
    HtmlContainer parent
    HtmlPage page
    HtmlApplication application
    String name = Utils.generateRandomName()
    String className
    String label
    String info
    Boolean editable

    /**
     * Adds an event to the application this element belongs
     *
     * @param event instance of {@link HtmlEvent} to add
     * @since 0.1.0
     */
    void addEvent(HtmlEvent event) {
        this.page.addEvent(event)
    }

    /**
     * Returns a list of the events associated to this element
     *
     * @return a list of {@link HtmlEvent} associated to this element
     * @since 0.1.0
     */
    List<HtmlEvent> listEvents() {
        return this.page.listEventsByComponentName(this.name)
    }

    /**
     * Used at any level to check whether the application is in dev mode
     *
     * @return true if the application is in development mode false otherwise
     * @since 0.1.0
     */
    boolean isDevelopment() {
        return this.application.isDevelopment()
    }

    /**
     * Utility method that can be used by templates to render html in presence or
     * absence of some class in this element
     *
     * @param className name present in the element
     * @return true if className is present in the element false otherwise
     * @since 0.1.0
     */
    boolean hasClass(String className) {
        return this.className?.split(" ")?.contains(className)
    }

    /**
     * Utility method that can be used by templates to render html in presence or
     * absence of some class in the parent element
     *
     * @param className name present in the parent element
     * @return true if className is present in the parent element false otherwise
     * @since 0.1.0
     */
    boolean hasParentClass(String className) {
        return this.parent?.hasClass(className)
    }

    /**
     * Utility method that can be used by templates to know whether the parent element
     * type is of a certain type.
     *
     * @param simpleName simple class name of the parent element
     * @return true if the parent type simple name matches the param or false otherwise
     * @since 0.1.0
     */
    boolean isParentType(String simpleName) {
        return simpleName == this.parent?.class?.simpleName
    }

    /**
     * Used by templates to process class names.
     *
     * - If no className has been provided by the user, it will use the base className provided in the template
     * - If className has been provided and it is prefixed with '+' then the provided className will be appended
     *   to the className provided by the template
     * - Otherwise the className provided will be applied
     *
     * @param base the base classes applied to the html element
     * @return combination of base and custom classes provided
     * @since 0.1.0
     */
    String classNames(String base) {
        if (!className) {
            return base
        }

        if (className && className ==~ "\\+.*") {
            return "$base ${className.replace("+", "")}"
        }

        return className
    }
}
