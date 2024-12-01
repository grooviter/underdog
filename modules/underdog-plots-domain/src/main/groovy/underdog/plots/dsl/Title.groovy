package underdog.plots.dsl

import underdog.plots.ast.Node
import groovy.transform.CompileStatic

/**
 * Title component, including main title and subtitle.
 *
 * @since 0.1.0
 */
@CompileStatic
@Node(hasId = true)
class Title {
    boolean show
    String text
    String link
    String target
    TextStyle textStyle
    String subtext
    String sublink
    String subtarget
    TextStyle subtextStyle
    String textAlign
    String textVerticalAlign
    boolean triggerEvent
    Integer padding
    Integer itemGap
    Integer zlevel
    Integer z
    String left
    String top
    String right
    String bottom
    String backgroundColor
    String borderColor
    Integer borderWidth
    Integer borderRadius
    Integer shadowBlur
    String shadowColor
    Integer shadowOffsetX
    Integer shadowOffsetY
}
