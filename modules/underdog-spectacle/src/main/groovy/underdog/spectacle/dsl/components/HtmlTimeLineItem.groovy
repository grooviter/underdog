package underdog.spectacle.dsl.components

import underdog.spectacle.dsl.HtmlElement

/**
 * Represents a step in a {@link HtmlTimeLine} component
 *
 * @since 0.1.0
 */
class HtmlTimeLineItem extends HtmlElement {
    String when
    String title
    String description
    String icon
    String iconText
    String iconBackground
}
