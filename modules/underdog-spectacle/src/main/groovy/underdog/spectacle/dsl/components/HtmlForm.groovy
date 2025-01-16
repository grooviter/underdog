package underdog.spectacle.dsl.components

import underdog.spectacle.dsl.HtmlContainer
import underdog.spectacle.dsl.HtmlEvent

class HtmlForm extends HtmlContainer {

    void onSubmit(List<String> inputs, List<String> outputs, Closure function) {
        def event = HtmlEvent.builder()
            .name('submit')
            .htmlFieldName(this.name)
            .function(function)
            .inputList(inputs)
            .outputList(outputs)
            .build()

        this.addEvent(event)
    }

    HtmlEvent getOnSubmit() {
        return this.listEvents().find { it.name == 'submit' }
    }
}
