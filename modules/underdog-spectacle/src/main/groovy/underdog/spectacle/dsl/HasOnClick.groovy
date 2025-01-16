package underdog.spectacle.dsl

import groovy.transform.SelfType

/**
 * Trait that adds the onClick event to any {@link HtmlElement}
 *
 * @since 0.1.0
 */
@SelfType(HtmlElement)
trait HasOnClick {

    /**
     * Adds a {@link HtmlEvent} to the application assigned to the
     * component the method has been attached.
     *
     * @param inputs inputs this event refers to
     * @param outputs ids of the elements affected by this event
     * @param function code to execute when triggering this event
     * @since 0.1.0
     */
    void onClick(
        List<String> inputs,
        List<String> outputs,
        Closure function) {

        def event = HtmlEvent.builder()
            .name('click')
            .htmlFieldName(this.name)
            .function(function)
            .inputList(inputs)
            .outputList(outputs)
            .build()

        this.addEvent(event)
    }
}
