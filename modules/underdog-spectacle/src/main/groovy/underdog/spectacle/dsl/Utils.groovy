package underdog.spectacle.dsl

/**
 * Util functions
 *
 * @since 0.1.0
 */
class Utils {
    /**
     * Generates a new random html element name. This function allows an argument so it
     * could be used in closures requiring an argument
     *
     * @param _unused this param is not used
     * @return a new random string with 'spectacle-' as prefix
     * @since 0.1.0
     */
    static String generateRandomName(String _unused) {
        return "spectacle-${generateRandomSuffix()}"
    }

    /**
     * Generates a new random html element name
     *
     * @return a new random string with 'spectacle-' as prefix
     * @since 0.1.0
     */
    static String generateRandomName() {
        return "spectacle-${generateRandomSuffix()}"
    }

    /**
     * Generates a new random id
     *
     * @return a new random string
     * @since 0.1.0
     */
    static String generateRandomSuffix() {
        return new Random().nextLong(100_000).toString().md5()
    }
}
