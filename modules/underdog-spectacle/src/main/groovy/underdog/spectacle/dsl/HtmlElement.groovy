package underdog.spectacle.dsl

/**
 * Represents an HTML element
 *
 * @since 0.1.0
 */
abstract class HtmlElement {
    HtmlApplication application
    String name = Utils.generateRandomName()
    String label
    Boolean editable

    /**
     * Adds an event to the application this element belongs
     *
     * @param event instance of {@link HtmlEvent} to add
     * @since 0.1.0
     */
    void addEvent(HtmlEvent event) {
        this.application.addEvent(event)
    }

    /**
     * Returns a list of the events associated to this element
     *
     * @return a list of {@link HtmlEvent} associated to this element
     * @since 0.1.0
     */
    List<HtmlEvent> listEvents() {
        return this.application.listEventsByComponentName(this.name)
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
}
