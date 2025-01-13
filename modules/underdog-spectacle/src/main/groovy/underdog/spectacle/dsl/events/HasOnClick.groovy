package underdog.spectacle.dsl.events

import groovy.transform.SelfType
import underdog.spectacle.dsl.HtmlElement
import underdog.spectacle.dsl.HtmlEvent

@SelfType(HtmlElement)
trait HasOnClick {

    void onClick(
        String function,
        List<String> inputs,
        List<String> outputs) {
        def event = HtmlEvent.builder()
            .name('click')
            .htmlFieldName(this.name)
            .functionName(function)
            .inputList(inputs)
            .outputList(outputs)
            .build()

        this.eventList.add(event)
    }
}
