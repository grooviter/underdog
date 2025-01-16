package underdog.spectacle.dsl

import groovy.transform.builder.Builder

@Builder(excludes = ['path'])
class HtmlEvent {
    String name
    Closure function
    String htmlFieldName
    List<String> inputList
    List<String> outputList

    String getPath() {
        return "/events/$htmlFieldName"
    }
}
