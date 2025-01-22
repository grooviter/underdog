package underdog.spectacle.dsl

import groovy.transform.NamedParam
import groovy.transform.NamedVariant
import underdog.DataFrame
import underdog.plots.Options
import underdog.spectacle.dsl.components.HtmlCard
import underdog.spectacle.dsl.components.HtmlChart
import underdog.spectacle.dsl.components.HtmlButton
import underdog.spectacle.dsl.components.HtmlColumn
import underdog.spectacle.dsl.components.HtmlDataFrame
import underdog.spectacle.dsl.components.HtmlDiv
import underdog.spectacle.dsl.components.HtmlForm
import underdog.spectacle.dsl.components.HtmlInputNumber
import underdog.spectacle.dsl.components.HtmlInputText
import underdog.spectacle.dsl.components.HtmlMarkdown
import underdog.spectacle.dsl.components.HtmlNumberCard
import underdog.spectacle.dsl.components.HtmlOptionGroup
import underdog.spectacle.dsl.components.HtmlRange
import underdog.spectacle.dsl.components.HtmlResetLink
import underdog.spectacle.dsl.components.HtmlRow
import underdog.spectacle.dsl.components.HtmlSelect
import underdog.spectacle.dsl.components.HtmlSpec
import underdog.spectacle.dsl.components.HtmlTextArea

/**
 * Represents any container containing {@link HtmlElement} children or more {@link HtmlContainer} instances
 *
 * @since 0.1.0
 */
abstract class HtmlContainer extends HtmlElement {
    List<HtmlElement> children = []

    /**
     * Utility method to add a child element to a container and setting the parent element
     * of the child to the current container
     *
     * @param child {@link HtmlElement} to add as a child element
     * @since 0.1.0
     */
    void addChild(HtmlElement child) {
        this.children.add(child)
        child.parent = this
    }

    /**
     * Creates a {@link HtmlSpec} element. This spec element is a convention layout on how
     * inputs, outputs and examples should be rendered by default. A spec can contain three
     * different blocks: inputs, outputs and examples
     *
     * <code>
     * spec {
     *     inputs {
     *         // input elements here
     *     }
     *     outputs {
     *         // output elements here
     *     }
     *     examples = [] // a list of maps representing input values examples
     * }
     * </code>
     *
     * @param closure children elements
     * @return an instance of {@link HtmlSpec}
     * @since 0.1.0
     */
    HtmlSpec spec(@DelegatesTo(HtmlSpec) Closure closure) {
        return new HtmlSpec(application: this.application, parent: this)
            .tap { with(closure) }
            .tap { it.initLayout() }
            .tap {this.addChild(it) }
    }

    /**
     * Adds a new row container to the current html page
     *
     * @param className html class attribute
     * @param closure DSL of the content of this container
     * @return an instance of {@link HtmlRow}
     * @since 0.1.0
     */
    @NamedVariant
    HtmlRow row(
        @NamedParam(required = false) String className = "",
        @DelegatesTo(HtmlContainer) Closure closure
    ) {
        return new HtmlRow(
            application: this.application,
            className: className,
            parent: this
        )
        .tap { with(closure) }
        .tap {this.addChild(it) }
    }

    /**
     * Adds a new html div element
     *
     * @param className html class attribute
     * @param closure elements nested in this div
     * @return an instance of {@link HtmlDiv}
     * @since 0.1.0
     */
    @NamedVariant
    HtmlDiv div(
        @NamedParam(required = false) String className = "",
        @DelegatesTo(HtmlContainer) Closure closure
    ) {
        return new HtmlDiv(
            application: this.application,
            parent: this,
            className: className
        )
        .tap { with(closure) }
        .tap {this.addChild(it) }
    }

    /**
     * Adds a new column container
     *
     * @param className html class attribute
     * @param closure DSL of the content of this container
     * @return an instance of {@link HtmlColumn}
     * @since 0.1.0
     */
    @NamedVariant
    HtmlColumn col(
        @NamedParam(required = false) String className = "",
        @DelegatesTo(HtmlContainer
    ) Closure closure
    ) {
        return new HtmlColumn(
            application: this.application,
            className: className,
            parent: this
        )
        .tap { with(closure) }
        .tap { this.addChild(it) }
    }

    /**
     * Adds a {@link HtmlCard}
     *
     * @param className html class attribute
     * @param closure nested html elements
     * @return an instance of {@link HtmlCard}
     * @since 0.1.0
     */
    @NamedVariant
    HtmlCard card(
        @NamedParam(required = false) String className = "",
        @DelegatesTo(HtmlCard) Closure closure) {
        return new HtmlCard(
            application: this.application,
            parent: this,
            className: className
        )
        .tap { with(closure) }
        .tap { this.addChild(it) }
    }

    /**
     * Adds a new html form container
     *
     * @param closure DSL of the content of this container
     * @return an instance of {@link HtmlForm}
     * @since 0.1.0
     */
    HtmlForm form(@DelegatesTo(HtmlForm) Closure closure) {
        return new HtmlForm(application: this.application, parent: this)
            .tap { with(closure) }
            .tap { this.addChild(it) }
    }

    /**
     * Adds a new html button
     *
     * @param text text of the button
     * @param name name of the element
     * @param className the css class names
     * @param editable whether the element is editable or not
     * @param closure DSL of the content of this container
     * @return an instance of {@link HtmlButton}
     * @since 0.1.0
     */
    @NamedVariant
    HtmlButton button(
        @NamedParam String text,
        @NamedParam(required = false) String name = Utils.generateRandomName(),
        @NamedParam(required = false) String className = "",
        @NamedParam(required = false) boolean editable = true,
        @DelegatesTo(HtmlButton) Closure closure
    ){
        return new HtmlButton(
            application: this.application,
            name: name,
            className: className,
            text: text,
            editable: editable
        )
        .tap { with(closure) }
        .tap { this.addChild(it) }
        .tap { this.application.addElement(it) }
    }

    /**
     * Adds a new html button
     *
     * @param text text of the button
     * @param name name of the element
     * @param className the css class names
     * @param editable whether the element is editable or not
     * @return an instance of {@link HtmlButton}
     * @since 0.1.0
     */
    @NamedVariant
    HtmlButton button(
        @NamedParam String text,
        @NamedParam(required = false) String name = Utils.generateRandomName(),
        @NamedParam(required = false) String className = "",
        @NamedParam(required = false) boolean editable = true
    ){
        return new HtmlButton(
            application: this.application,
            name: name,
            className: className,
            text: text,
            editable: editable
        )
        .tap { this.addChild(it) }
        .tap { this.application.addElement(it) }
    }

    /**
     * Creates a reset button with the appearance of a link
     *
     * @param text the name of the link
     * @param name name of the html component
     * @param editable whether if it is editable or not
     * @return an instance of {@link HtmlResetLink}
     * @since 0.1.0
     */
    @NamedVariant
    HtmlResetLink resetLink(
        @NamedParam String text,
        @NamedParam(required = false) String name = Utils.generateRandomName(),
        @NamedParam(required = false) String className = "",
        @NamedParam(required = false) boolean editable = true
    ){
        return new HtmlResetLink(
            application: this.application,
            name: name,
            className: className,
            text: text,
            editable: editable
        )
        .tap { this.addChild(it) }
        .tap { this.application.addElement(it) }
    }

    /**
     * Adds a new number input
     *
     * @param name the name of the element
     * @param label the label of the input element
     * @param editable whether the element is editable or not
     * @param value default value of the element
     * @return an instance of {@link HtmlInputNumber}
     * @since 0.1.0
     */
    @NamedVariant
    HtmlInputNumber number(
        @NamedParam(required = false) String name = Utils.generateRandomName(),
        @NamedParam(required = false) String label = name,
        @NamedParam(required = false) String info = "",
        @NamedParam(required = false) boolean editable = true,
        @NamedParam(required = false) Number value = 0
    ) {
        return new HtmlInputNumber(
            application: this.application,
            label: label,
            name: name,
            info: info,
            editable: editable,
            value: value
        )
        .tap { this.addChild(it) }
        .tap { this.application.addElement(it) }
    }

    /**
     * Renders an HTML range input
     *
     * @param name html name of the component
     * @param label html label of the range component
     * @param className class html attribute
     * @param info info about what the element content is about
     * @param value default value
     * @return an instance of {@link HtmlRange}
     * @since 0.1.0
     */
    @NamedVariant
    HtmlRange range(
        @NamedParam(required = false) String name = Utils.generateRandomName(),
        @NamedParam(required = false) String label = name,
        @NamedParam(required = false) String className = "",
        @NamedParam(required = false) String info = "",
        @NamedParam(required = false) Number value = 50
    ) {
        return new HtmlRange(
            value: value,
            name: name,
            className: className,
            label: label,
            info: info
        )
        .tap { this.addChild(it) }
        .tap { this.application.addElement(it) }
    }

    /**
     * Adds a new input text element
     *
     * @param name the name of the element
     * @param label label of the element
     * @param placeHolder hint about the text field
     * @param editable whether the element is editable or not
     * @return
     * @since 0.1.0
     */
    @NamedVariant
    HtmlInputText text(
        @NamedParam(required = false) String name = Utils.generateRandomName(),
        @NamedParam(required = false) String label = name,
        @NamedParam(required = false) String info = "",
        @NamedParam(required = false) String placeHolder = "",
        @NamedParam(required = false) boolean editable = true
    ) {
        return new HtmlInputText(
            application: this.application,
            name: name,
            info: info,
            label: label,
            editable: editable,
            placeHolder: placeHolder
        )
        .tap { this.addChild(it) }
        .tap { this.application.addElement(it) }
    }

    /***
     * Adds an html select component
     *
     * @param name name of the html element
     * @param className class attribute value
     * @param info a little bit more info about the element
     * @param label label of the element
     * @param closure nested html element
     * @return an instance of {@link HtmlSelect}
     * @since 0.1.0
     */
    @NamedVariant
    HtmlSelect select(
        @NamedParam(required = false) String name = Utils.generateRandomName(),
        @NamedParam(required = false) String className = "",
        @NamedParam(required = false) String info = "",
        @NamedParam(required = false) String label = name,
        @DelegatesTo(HtmlSelect) Closure closure
    ) {
        return new HtmlSelect(
            name: name,
            className: className,
            info: info,
            label: label
        )
        .tap { with(closure) }
        .tap { this.addChild(it) }
        .tap { this.application.addElement(it) }
    }

    /**
     * Adds an html option group element
     *
     * @param name name of the html element
     * @param className html class attribute
     * @param label label of the input field
     * @param closure nested option elements
     * @return an instance of {@link HtmlOptionGroup}
     * @since 0.1.0
     */
    @NamedVariant
    HtmlOptionGroup optionGroup(
        @NamedParam(required = false) String name = Utils.generateRandomName(),
        @NamedParam(required = false) String className = "",
        @NamedParam(required = false) String info = "",
        @NamedParam(required = false) String label = name,
        @DelegatesTo(HtmlOptionGroup) Closure closure
    ) {
        return new HtmlOptionGroup(
            name: name,
            info: info,
            className: className,
            label: label
        )
        .tap { with(closure) }
        .tap { this.addChild(it) }
        .tap { this.application.addElement(it) }
    }

    /**
     * Adds a new text area element
     *
     * @param name the name of the element
     * @param label label of the element
     * @param value text area default value
     * @param editable whether the element is editable or not
     * @return an instance of {@link HtmlTextArea}
     * @since 0.1.0
     */
    @NamedVariant
    HtmlTextArea textArea(
        @NamedParam(required = false) String name = Utils.generateRandomName(),
        @NamedParam(required = false) String label = name,
        @NamedParam(required = false) String value = "",
        @NamedParam(required = false) Integer rows = 5,
        @NamedParam(required = false) boolean editable = true
    ) {
        return new HtmlTextArea(
            application: this.application,
            name: name,
            value: value,
            rows: rows,
            label: label,
            editable: editable
        )
        .tap { this.addChild(it) }
        .tap { this.application.addElement(it) }
    }

    /**
     * Adds an element which shows a dataframe
     *
     * @param name the name of the element
     * @param info
     * @param label the label of the element
     * @param editable whether the element is editable or not
     * @param value default value
     * @return an instance of {@link HtmlDataFrame}
     * @since 0.1.0
     */
    @NamedVariant
    HtmlDataFrame dataframe(
            @NamedParam(required = false) String name = Utils.generateRandomName(),
            @NamedParam(required = false) String label = name,
            @NamedParam(required = false) String info = "",
            @NamedParam(required = false) String className = "",
            @NamedParam(required = false) boolean editable = true,
            @NamedParam(required = false) DataFrame value = null
    ) {
        return new HtmlDataFrame(
            application: this.application,
            name: name,
            info: info,
            label: label,
            className: className,
            editable: editable,
            value: value
        )
        .tap { this.addChild(it) }
        .tap { this.application.addElement(it) }
    }

    /**
     * Element rendering an Underdog's chart
     *
     * @param name the name of the element
     * @param label
     * @param info
     * @param className
     * @param defaultValue default value to show the chart
     * @return an instance of {@link HtmlChart}
     * @since 0.1.0
     */
    @NamedVariant
    HtmlChart chart(
        @NamedParam(required = false) String name = Utils.generateRandomName(),
        @NamedParam(required = false) String label = "",
        @NamedParam(required = false) String info = "",
        @NamedParam(required = false) String className = "",
        @NamedParam(required = false) Object defaultValue = null,
        Closure<Options> supplier
    ) {
        return new HtmlChart(
            name: name,
            label: label,
            info: info,
            className: className,
            supplier: supplier,
            value: defaultValue
        )
        .tap { this.addChild(it) }
        .tap { this.application.addElement(it) }
    }

    /**
     * Represents a card showing a number with a title defining what that number means
     *
     * @param name the name of the component
     * @param title the title describing what the number means
     * @param symbol for example % or $
     * @param defaultValue the default value when the component is rendered
     * @return an instance of {@link HtmlNumberCard}
     * @since 0.1.0
     */
    @NamedVariant
    HtmlNumberCard numberCard(
        @NamedParam(required = false) String name = Utils.generateRandomName(),
        @NamedParam(required = false) String title = "N/A",
        @NamedParam(required = false) String symbol = "",
        @NamedParam(required = false) String className = "",
        @NamedParam(required = false) Number defaultValue = 0
    ) {
        return new HtmlNumberCard(
            name: name,
            title: title,
            symbol: symbol,
            className: className,
            value: defaultValue
        )
        .tap { this.addChild(it) }
        .tap { this.application.addElement(it) }
    }

    /**
     * Element which renders markdown text
     *
     * @param markdown markdown syntax text
     * @return an instance of {@link HtmlMarkdown}
     * @since 0.1.0
     */
    HtmlMarkdown markdown(String markdown) {
        return new HtmlMarkdown(value: markdown)
            .tap { this.addChild(it) }
            .tap { this.application.addElement(it) }
    }
}
