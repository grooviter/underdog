package underdog.spectacle.dsl

import groovy.transform.NamedParam
import groovy.transform.NamedVariant
import underdog.DataFrame
import underdog.plots.Options
import underdog.spectacle.dsl.components.HtmlAccordion
import underdog.spectacle.dsl.components.HtmlAudio
import underdog.spectacle.dsl.components.HtmlCard
import underdog.spectacle.dsl.components.HtmlChart
import underdog.spectacle.dsl.components.HtmlButton
import underdog.spectacle.dsl.components.HtmlChat
import underdog.spectacle.dsl.components.HtmlCheckboxGroup
import underdog.spectacle.dsl.components.HtmlColumn
import underdog.spectacle.dsl.components.HtmlDataFrame
import underdog.spectacle.dsl.components.HtmlDatePicker
import underdog.spectacle.dsl.components.HtmlDiv
import underdog.spectacle.dsl.components.HtmlForm
import underdog.spectacle.dsl.components.HtmlImage
import underdog.spectacle.dsl.components.HtmlInputNumber
import underdog.spectacle.dsl.components.HtmlInputText
import underdog.spectacle.dsl.components.HtmlMarkdown
import underdog.spectacle.dsl.components.HtmlNumberCard
import underdog.spectacle.dsl.components.HtmlOptionGroup
import underdog.spectacle.dsl.components.HtmlProgressIndeterminate
import underdog.spectacle.dsl.components.HtmlRange
import underdog.spectacle.dsl.components.HtmlResetLink
import underdog.spectacle.dsl.components.HtmlRow
import underdog.spectacle.dsl.components.HtmlSelect
import underdog.spectacle.dsl.components.HtmlSpec
import underdog.spectacle.dsl.components.HtmlSwitchGroup

import underdog.spectacle.dsl.components.HtmlTextArea
import underdog.spectacle.dsl.components.HtmlTimePicker

import java.time.LocalDate
import java.time.LocalDateTime

/**
 * Represents any container containing {@link HtmlElement} children or more {@link HtmlContainer} instances
 *
 * @since 0.1.0
 */
abstract class HtmlContainer extends HtmlElement {
    List<HtmlElement> children = []
    Map<String, String> extraAttributes = [:]

    /**
     * Utility method to add a child element to a container and setting the parent element
     * of the child to the current container
     *
     * @param child {@link HtmlElement} to add as a child element
     * @since 0.1.0
     */
    void addChild(HtmlElement child) {
        this.children.add(child)

        child.page = this.page
        child.parent = this
        child.application = this.application

        if (this instanceof HtmlPage) {
            child.page = this as HtmlPage
        }
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
        return new HtmlSpec()
            .tap {this.addChild(it) }
            .tap { with(closure) }
            .tap { it.initLayout() }
    }

    /**
     * Renders a chat room
     *
     * @param name name of the html element
     * @param title title of the chat room
     * @param inputLabel label of the chat input field
     * @param inputPlaceholder placeholder of the chat input field
     * @param closure function to execute when the input field is sent
     * @return an instance of {@link HtmlChat}
     * @since 0.1.0
     */
    @NamedVariant
    HtmlChat chat(
        @NamedParam(required = false) String name = Utils.generateRandomName(),
        @NamedParam(required = false) String title,
        @NamedParam(required = false) String inputLabel,
        @NamedParam(required = false) String inputPlaceholder,
        @DelegatesTo(HtmlChat) Closure closure
    ) {
        return new HtmlChat(
            name: name,
            title: title,
            inputLabel: inputLabel,
            inputPlaceHolder: inputPlaceholder,
        )
        .tap {this.addChild(it) }
        .tap { with(closure)}
        .tap { it.initLayout() }
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
        return new HtmlRow(className: className)
            .tap {this.addChild(it) }
            .tap { with(closure) }
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
        return new HtmlDiv(className: className)
            .tap {this.addChild(it) }
            .tap { with(closure) }
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
        @DelegatesTo(HtmlContainer) Closure closure
    ) {
        return new HtmlColumn(className: className)
            .tap { this.addChild(it) }
            .tap { with(closure) }
    }

    /**
     * Renders an html audio field
     *
     * @param name the name of the html element
     * @param label the label of the html element
     * @param info some information about what the element is for
     * @param path the path where the audio file can be accessed
     * @return an instance of type {@link HtmlAudio}
     * @since 0.1.0
     */
    @NamedVariant
    HtmlAudio audio(
        @NamedParam(required = false) String name = Utils.generateRandomName(),
        @NamedParam(required = false) String label = "",
        @NamedParam(required = false) String info = "",
        @NamedParam(required = false) String path = ""
    ) {
        return new HtmlAudio(
            name: name,
            info: info,
            label: label,
            value: path
        )
        .tap { this.addChild(it) }
        .tap { this.application.addElement(it) }
    }

    /**
     * Renders an accordion html element. Useful for grouping nested elements
     *
     * @param name name of the html component
     * @param closure nested elements
     * @return an instance of {@link HtmlAccordion}
     * @since 0.1.0
     */
    @NamedVariant
    HtmlAccordion accordion(
        @NamedParam(required = false) String name = Utils.generateRandomName(),
        @DelegatesTo(HtmlAccordion) Closure closure
    ) {
        return new HtmlAccordion(name: name)
            .tap { this.addChild(it) }
            .tap { with(closure) }
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
        return new HtmlCard(className: className)
            .tap { this.addChild(it) }
            .tap { with(closure) }
    }

    /**
     * Adds a new html form container
     *
     * @param indicatorSelector CSS selector to use for busy type elements when executing a request
     * @param streaming whether the form is going to be streaming data to output fields or not
     * @param closure DSL of the content of this container
     * @return an instance of {@link HtmlForm}
     * @since 0.1.0
     */
    @NamedVariant
    HtmlForm form(
        @NamedParam(required = false) String name = Utils.generateRandomName(),
        @NamedParam(required = false) String indicatorSelector = "",
        @NamedParam(required = false) Boolean streaming = false,
        @DelegatesTo(HtmlForm) Closure closure
    ) {
        return new HtmlForm(name: name, streaming: streaming, indicatorSelector: indicatorSelector)
            .tap { this.addChild(it) }
            .tap { with(closure) }
    }

    /**
     * Renders an image passing a url as its value
     *
     * @param name the name of the element
     * @param label the label of the element
     * @param info info about what the element content is about
     * @param className class attribute of the html element
     * @param value path or full url where the image is located
     * @return an instance of {@link HtmlImage}
     * @since 0.1.0
     */
    @NamedVariant
    HtmlImage image(
        @NamedParam(required = false) String name = Utils.generateRandomName(),
        @NamedParam(required = false) String label = '',
        @NamedParam(required = false) String info = '',
        @NamedParam(required = false) String className = '',
        @NamedParam(required = false) String value = ''
    ) {
        return new HtmlImage(
            name: name,
            label: label,
            className: className,
            info: info,
            value: value
        )
        .tap { this.addChild(it) }
        .tap { this.application.addElement(it) }
    }

    /**
     * Adds a new html button
     *
     * @param text text of the button
     * @param name name of the element
     * @param className the css class names
     * @param iconName creates a nested <i> element
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
        @NamedParam(required = false) String iconName = "",
        @NamedParam(required = false) boolean editable = true,
        @DelegatesTo(HtmlButton) Closure closure
    ){
        return new HtmlButton(
            name: name,
            className: className,
            icon: iconName,
            text: text,
            editable: editable
        )
        .tap { this.addChild(it) }
        .tap { this.application.addElement(it) }
        .tap { with(closure) }
    }

    /**
     * Adds the {@link HtmlElement} passed to the current {@link HtmlContainer}
     *
     * @param element an instance of {@link HtmlElement}
     * @return the instance passed as argument initialized with the current container
     * @since 0.1.0
     */
    <U extends HtmlElement> U element(U element){
        return element.tap {
            this.addChild(it)
            application.addElement(it)
        }
    }

    /**
     * Adds a new html button
     *
     * @param text text of the button
     * @param name name of the element
     * @param className the css class names
     * @param iconName icon name follows bootstrap icon syntax, for example 'bi bi-easel'
     * @param editable whether the element is editable or not
     * @return an instance of {@link HtmlButton}
     * @since 0.1.0
     */
    @NamedVariant
    HtmlButton button(
        @NamedParam String text,
        @NamedParam(required = false) String name = Utils.generateRandomName(),
        @NamedParam(required = false) String className = "",
        @NamedParam(required = false) String iconName = "",
        @NamedParam(required = false) boolean editable = true
    ){
        return new HtmlButton(
            name: name,
            className: className,
            icon: iconName,
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
     * @param info info about what the element content is about
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
     * @param min lower bound
     * @param max upper bound
     * @param step how wide is each step between lower and upper bound
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
        @NamedParam(required = false) Number min = 0,
        @NamedParam(required = false) Number max = 100,
        @NamedParam(required = false) Number step = 25,
        @NamedParam(required = false) Number value = 50,
        @NamedParam(required = false) String symbol = "",
        @NamedParam(required = false) Boolean showUpdatedValue = false,
        @NamedParam(required = false) Boolean showMarkers = false
    ) {
        return new HtmlRange(
            value: value,
            name: name,
            className: className,
            label: label,
            info: info,
            min: min,
            max: max,
            step: step,
            symbol: symbol,
            showUpdatedValue: showUpdatedValue,
            showMarkers: showMarkers
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
     * @return an instance of {@link HtmlInputText}
     * @since 0.1.0
     */
    @NamedVariant
    HtmlInputText text(
        @NamedParam(required = false) String name = Utils.generateRandomName(),
        @NamedParam(required = false) String label = name,
        @NamedParam(required = false) String info = "",
        @NamedParam(required = false) String placeHolder = "",
        @NamedParam(required = false) String icon="",
        @NamedParam(required = false) String prefix = "",
        @NamedParam(required = false) String suffix = "",
        @NamedParam(required = false) Boolean required = false,
        @NamedParam(required = false) boolean editable = true
    ) {
        return new HtmlInputText(
            name: name,
            info: info,
            label: label,
            icon: icon,
            suffix: suffix,
            prefix: prefix,
            required: required,
            editable: editable,
            placeHolder: placeHolder
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
     * @param onEnter action executed when typing enter
     * @return an instance of {@link HtmlInputText}
     * @since 0.1.0
     */
    @NamedVariant
    HtmlInputText text(
        @NamedParam(required = false) String name = Utils.generateRandomName(),
        @NamedParam(required = false) String label = name,
        @NamedParam(required = false) String info = "",
        @NamedParam(required = false) String placeHolder = "",
        @NamedParam(required = false) String icon="",
        @NamedParam(required = false) String prefix = "",
        @NamedParam(required = false) String suffix = "",
        @NamedParam(required = false) Boolean required = false,
        @NamedParam(required = false) boolean editable = true,
        @DelegatesTo(HtmlInputText) Closure onEnter
    ) {
        return new HtmlInputText(
            name: name,
            info: info,
            label: label,
            icon: icon,
            suffix: suffix,
            prefix: prefix,
            required: required,
            editable: editable,
            placeHolder: placeHolder
        )
        .tap { this.addChild(it) }
        .tap { this.application.addElement(it) }
        .tap { with(onEnter) }
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
        .tap { this.addChild(it) }
        .tap { this.application.addElement(it) }
        .tap { with(closure) }
    }

    /**
     * Adds an html option group element
     *
     * @param name name of the html element
     * @param className html class attribute
     * @param label label of the input field
     * @param info some description about the element
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
        .tap { this.addChild(it) }
        .tap { this.application.addElement(it) }
        .tap { with(closure) }
    }

    /**
     * Renders a progress bar with an indeterminate duration. Useful for
     * showing progress of a process with an unknown duration.
     *
     * @param name of the html element
     * @param className class attribute of the element
     * @param display whether to show or hide the element
     * @return an instance of {@link HtmlProgressIndeterminate}
     * @since 0.1.0
     */
    @NamedVariant
    HtmlProgressIndeterminate progressIndeterminate(
        @NamedParam(required = false) String name = Utils.generateRandomName(),
        @NamedParam(required = false) String className = "",
        @NamedParam(required = false) Boolean display = false
    ){
        return new HtmlProgressIndeterminate(
            name: name,
            className: className,
            display: display
        )
        .tap { this.addChild(it) }
        .tap { this.application.addElement(it) }
    }

    /**
     * Adds an html checkbox group element
     *
     * @param name name of the html element
     * @param className html class attribute
     * @param info some description about the element
     * @param label label of the input field
     * @param closure nested option elements
     * @return an instance of {@link HtmlCheckboxGroup}
     * @since 0.1.0
     */
    @NamedVariant
    HtmlCheckboxGroup checkboxGroup(
        @NamedParam(required = false) String name = Utils.generateRandomName(),
        @NamedParam(required = false) String className = "",
        @NamedParam(required = false) String info = "",
        @NamedParam(required = false) String label = name,
        @DelegatesTo(HtmlCheckboxGroup) Closure closure
    ) {
        return new HtmlCheckboxGroup(
            name: name,
            info: info,
            className: className,
            label: label
        )
        .tap { this.addChild(it) }
        .tap { this.application.addElement(it) }
        .tap { with(closure) }
    }

    /**
     * Adds an html checkbox group element with the appearance of a switch panel
     *
     * @param name name of the html element
     * @param className html class attribute
     * @param info some description about the element
     * @param label label of the input field
     * @param closure nested option elements
     * @return an instance of {@link HtmlSwitchGroup}
     * @since 0.1.0
     */
    @NamedVariant
    HtmlSwitchGroup switchGroup(
        @NamedParam(required = false) String name = Utils.generateRandomName(),
        @NamedParam(required = false) String className = "",
        @NamedParam(required = false) String info = "",
        @NamedParam(required = false) String label = "",
        @DelegatesTo(HtmlCheckboxGroup) Closure closure
    ) {
        return new HtmlSwitchGroup(
            name: name,
            info: info,
            className: className,
            label: label
        )
        .tap { this.addChild(it) }
        .tap { this.application.addElement(it) }
        .tap { with(closure) }
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
        @NamedParam(required = false) String info = "",
        @NamedParam(required = false) Integer rows = 5,
        @NamedParam(required = false) Boolean required = false,
        @NamedParam(required = false) boolean editable = true
    ) {
        return new HtmlTextArea(
            name: name,
            value: value,
            info: info,
            required: required,
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
     * @param info information about the element
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
     * @param label label of the element
     * @param info information about the element
     * @param className class attribute of the html element
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
     * @param label label of the html element
     * @param info some information about the component
     * @return an instance of {@link HtmlMarkdown}
     * @since 0.1.0
     */
    @NamedVariant
    HtmlMarkdown markdown(
        @NamedParam(required = false) String markdown = "",
        @NamedParam(required = false) String label = "",
        @NamedParam(required = false) String info = "",
        @NamedParam(required = false) String name = Utils.generateRandomName()
    ) {
        return new HtmlMarkdown(
            name: name,
            label: label,
            info: info,
            value: markdown
        )
        .tap { this.addChild(it) }
        .tap { this.application.addElement(it) }
    }

    /**
     * Renders a date input field
     *
     * @param name name of the html element
     * @param label label of the element
     * @param info information about the element
     * @param from lower bound
     * @param to upper bound
     * @return an instance of {@link HtmlDatePicker}
     * @since 0.1.0
     */
    @NamedVariant
    HtmlDatePicker datePicker(
        @NamedParam(required = false) String name = Utils.generateRandomName(),
        @NamedParam(required = false) String label = "",
        @NamedParam(required = false) String info = "" ,
        @NamedParam(required = false) LocalDate from = LocalDate.now(),
        @NamedParam(required = false) LocalDate to = LocalDate.now().plusDays(1)
    ) {
        return new HtmlDatePicker(
            name: name,
            label: label,
            info: info,
            from: from,
            to: to
        )
        .tap { this.addChild(it) }
        .tap { this.application.addElement(it) }
    }

    /**
     * Renders a time input field
     *
     * @param name name of the html element
     * @param label label of the element
     * @param info information about the element
     * @param from lower bound
     * @param to upper bound
     * @return an instance of {@link HtmlTimePicker}
     * @since 0.1.0
     */
    @NamedVariant
    HtmlTimePicker timePicker(
        @NamedParam(required = false) String name = Utils.generateRandomName(),
        @NamedParam(required = false) String label = "",
        @NamedParam(required = false) String info = "" ,
        @NamedParam(required = false) LocalDateTime from = LocalDateTime.now(),
        @NamedParam(required = false) LocalDateTime to = LocalDateTime.now().plusDays(1)
    ) {
        return new HtmlTimePicker(
            name: name,
            label: label,
            info: info,
            from: from,
            to: to
        )
        .tap { this.addChild(it) }
        .tap { this.application.addElement(it) }
    }
}
