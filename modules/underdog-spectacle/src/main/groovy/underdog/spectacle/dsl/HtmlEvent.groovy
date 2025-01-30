package underdog.spectacle.dsl

import groovy.transform.builder.Builder

/**
 * Represents any event produced by Spectacle's components
 *
 * @since 0.1.0
 */
@Builder(excludes = ['path'])
class HtmlEvent {
    String name
    Closure function
    Boolean streaming
    String htmlFieldName
    List<String> inputList
    List<String> outputList

    /**
     * Returns true if this event is not streaming related
     *
     * @return true if this event is not streaming related
     * @since 0.1.0
     */
    Boolean isNotStreaming() {
        return !this.streaming
    }

    /**
     * Returns true if this event is streaming related
     *
     * @return true if this event is streaming related
     * @since 0.1.0
     */
    Boolean isStreaming() {
        return this.streaming
    }

    /**
     * Returns the url path where the event will be exposed
     *
     * @return the path part of a given URL where the event functionality will be exposed
     * @since 0.1.0
     */
    String getPath() {
        return "/events/$htmlFieldName"
    }
}
