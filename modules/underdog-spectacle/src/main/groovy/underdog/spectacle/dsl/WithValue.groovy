package underdog.spectacle.dsl

trait WithValue<T> {
    /**
     * Represents the element's value
     *
     * @since 0.1.0
     */
    T value

    /**
     * Whether the element must have value or not
     *
     * @since 0.1.0
     */
    Boolean required
}