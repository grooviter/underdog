package underdog.spectacle.dsl

/**
 * Represents the execution context.
 *
 * - data coming from the UI
 * - application configuration
 * - access to underlying HTTP classes
 *
 * @since 0.1.0
 */
abstract class Context {
    /**
     * Retrieves a given param value as a string and it could return a default value in case
     * param has not been found
     *
     * @param fieldName the name of the param you want to retrieve
     * @param defaultValue the value in case the expected param has not been found
     * @return the expected value, the default value, or null
     * @since 0.1.0
     */
    abstract String param(String fieldName, String defaultValue = null)

    /**
     *
     * @param fieldName the name of the param you want to retrieve
     * @param defaultValue the value in case the expected param has not been found
     * @return the expected value, the default value, or null
     * @since 0.1.0
     */
    abstract Double paramDouble(String fieldName, Double defaultValue = null)

    /**
     *
     * @param fieldName the name of the param you want to retrieve
     * @param defaultValue the value in case the expected param has not been found
     * @return the expected value, the default value, or null
     * @since 0.1.0
     */
    abstract Integer paramInteger(String fieldName, Integer defaultValue = null)

    /**
     * @param fieldName the name of the param you want to retrieve
     * @param defaultValue the value in case the expected param has not been found
     * @return the expected value, the default value, or null
     * @since 0.1.0
     */
    abstract Double pDouble(String fieldName, Double defaultValue = null)

    /**
     * @param fieldName the name of the param you want to retrieve
     * @param defaultValue the value in case the expected param has not been found
     * @return the expected value, the default value, or null
     * @since 0.1.0
     */
    abstract Integer pInteger(String fieldName, Integer defaultValue = null)

    /**
     * Gets the URL of a resource in the classpath
     *
     * @param path path in the classpath of the resource we want to get the URL from
     * @return a URL if the resource is found null otherwise
     * @since 0.1.0
     */
    abstract URL resource(String path)

    /**
     * Gets the application configuration
     *
     * @return a {@link Map} with the application configuration
     * @since 0.1.0
     */
    abstract Map<String,?> getConfiguration()
}