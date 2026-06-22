package underdog.spectacle.dsl.components

import groovy.transform.NamedParam
import groovy.transform.NamedVariant
import underdog.spectacle.dsl.HtmlContainer
import underdog.spectacle.dsl.HtmlEvent

class HtmlForm extends HtmlContainer {
    String indicatorSelector = ""
    String disabledSelector = ""
    Boolean streaming

    @NamedVariant
    void onSubmit(
        @NamedParam(required = true) List<String> inputs,
        @NamedParam(required = true) List<String> outputs,
        Closure function
    ) {
        def event = HtmlEvent.builder()
            .name('submit')
            .htmlFieldName(this.name)
            .function(function)
            .streaming(streaming)
            .inputList(inputs)
            .outputList(outputs)
            .build()

        this.addEvent(event)
    }

    Boolean isStreaming() {
        return this.streaming
    }

    HtmlEvent getOnSubmit() {
        return this.listEvents().find { it.name == 'submit' }
    }
}
