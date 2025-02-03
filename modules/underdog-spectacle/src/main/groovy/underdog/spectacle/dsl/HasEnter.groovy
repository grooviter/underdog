package underdog.spectacle.dsl

import groovy.transform.SelfType

/**
 * Represents the event of typing the enter key on an html component
 *
 * @since 0.1.0
 */
@SelfType(HtmlElement)
trait HasEnter {
    /**
     * Adds a key enter {@link HtmlEvent} to the application assigned to the
     * component the method has been attached.
     *
     * @param inputs inputs this event refers to
     * @param outputs ids of the elements affected by this event
     * @param function code to execute when triggering this event
     * @since 0.1.0
     */
    void onEnter(
        List<String> inputs,
        List<String> outputs,
        Boolean streaming = false,
        Closure function
    ) {
        def event = HtmlEvent.builder()
            .name('enter')
            .htmlFieldName(this.name)
            .function(function)
            .streaming(streaming)
            .inputList(inputs)
            .outputList(outputs)
            .build()

        this.addEvent(event)
    }
}