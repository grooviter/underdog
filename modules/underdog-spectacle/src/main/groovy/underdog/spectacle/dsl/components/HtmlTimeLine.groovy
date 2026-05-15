package underdog.spectacle.dsl.components

import groovy.transform.NamedParam
import groovy.transform.NamedVariant
import underdog.spectacle.dsl.HtmlContainer

/**
 * Represents a line of events during a time line
 *
 * @since 0.1.0
 */
class HtmlTimeLine extends HtmlContainer<HtmlTimeLineItems> {

    /**
     * When using the component as a target in an event the value
     * provided should be of this type
     *
     * @since 0.1.0
     */
    static class HtmlTimeLineItems {
        /**
         * Items of the timeline
         *
         * @since 0.1.0
         */
        List<HtmlTimeLineItem> items = []
    }

    /**
     * Renders a new timeline item inside the current timeline
     *
     * @param title the title of the timeline item
     * @param description the description of the timeline item
     * @param when this is the field where normally you would insert when this item happened
     * @param icon you can use bootstrap icons syntax (bi bi-<NAME_OF_THE_ICON>)
     * @param iconText instead of an icon you can set a text
     * @param iconBackground the color of the icon area background
     * @return an instance of {@link HtmlTimeLineItem}
     * @since 0.1.0
     */
    @NamedVariant
    HtmlTimeLineItem item(
        @NamedParam(required = true) String title,
        @NamedParam(required = true) String description,
        @NamedParam(required = false) String when,
        @NamedParam(required = false) String icon,
        @NamedParam(required = false) String iconText,
        @NamedParam(required = false) String iconBackground
    ) {
        return new HtmlTimeLineItem(
            when: when,
            title: title,
            description: description,
            icon: icon,
            iconText: iconText,
            iconBackground: iconBackground
        )
        .tap {this.addChild(it)}
    }
}
