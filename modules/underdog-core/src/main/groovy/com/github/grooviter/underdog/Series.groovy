package com.github.grooviter.underdog

/**
 * @since 0.1.0
 */
interface Series extends Columnar {
    /**
     * @since 0.1.0
     */
    Series plus(Number o)

    /**
     * @since 0.1.0
     */
    Series plus(String st)

    /**
     * @since 0.1.0
     */
    Series plus(Series series)

    /**
     * @since 0.1.0
     */
    Object getImplementation()

    /**
     * @since 0.1.0
     */
    Object getAt(Integer index)

    /**
     * @since 0.1.0
     */
    Series getIloc()

    /**
     * @since 0.1.0
     */
    Long size()
}