package underdog.spectacle.dsl

import groovy.transform.builder.Builder

@Builder
class HtmlEvent {
    String name
    String functionName
    String htmlFieldName
    List<String> inputList
    List<String> outputList
}
