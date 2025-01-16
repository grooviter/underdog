package underdog.spectacle.dsl

/**
 * Represents an html element capable of receiving a value
 *
 * @since 0.1.0
 */
class HtmlElementWithValue<T> extends HtmlElement {
    /**
     * Represents the element's value
     *
     * @since 0.1.0
     */
    T value
}