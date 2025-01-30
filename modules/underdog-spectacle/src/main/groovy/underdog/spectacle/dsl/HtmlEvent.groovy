package underdog.spectacle.dsl

import groovy.transform.builder.Builder

@Builder(excludes = ['path'])
class HtmlEvent {
    String name
    Closure function
    Boolean streaming
    String htmlFieldName
    List<String> inputList
    List<String> outputList

    Boolean isNotStreaming() {
        return !this.streaming
    }

    Boolean isStreaming() {
        return this.streaming
    }

    String getPath() {
        return "/events/$htmlFieldName"
    }
}
