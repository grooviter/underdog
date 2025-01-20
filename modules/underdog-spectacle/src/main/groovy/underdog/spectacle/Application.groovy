package underdog.spectacle

/**
 * Represents an Spectable application
 *
 * @since 0.1.0
 */
interface Application {
    /**
     * Starts a new application
     *
     * @since 0.1.0
     */
    void launch()

    /**
     * Starts a new application in development mode
     *
     * @since 0.1.0
     */
    void dev()

    /**
     * Starts a new application in development mode
     *
     * @param toWatch directory to watch
     * @since 0.1.0
     */
    void dev(File toWatch)

    /**
     * Stops the application
     *
     * @since 0.1.0
     */
    void stop()
}
