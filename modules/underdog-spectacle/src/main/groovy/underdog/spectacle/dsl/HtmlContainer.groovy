package underdog.spectacle.dsl

import groovy.transform.NamedParam
import groovy.transform.NamedVariant
import underdog.plots.Options
import underdog.spectacle.dsl.components.HtmlChart
import underdog.spectacle.dsl.components.HtmlButton
import underdog.spectacle.dsl.components.HtmlColumn
import underdog.spectacle.dsl.components.HtmlDataFrame
import underdog.spectacle.dsl.components.HtmlForm
import underdog.spectacle.dsl.components.HtmlInputNumber
import underdog.spectacle.dsl.components.HtmlInputText
import underdog.spectacle.dsl.components.HtmlMarkdown
import underdog.spectacle.dsl.components.HtmlNumberCard
import underdog.spectacle.dsl.components.HtmlRow
import underdog.spectacle.dsl.components.HtmlTextArea

/**
 * Represents any container containing {@link HtmlElement} children or more {@link HtmlContainer} instances
 *
 * @since 0.1.0
 */
abstract class HtmlContainer extends HtmlElement {
    HtmlContainer parent
    List<HtmlElement> children = []

    /**
     * Adds a new row container to the current html page
     *
     * @param closure DSL of the content of this container
     * @return an instance of {@link HtmlRow}
     * @since 0.1.0
     */
    HtmlRow row(@DelegatesTo(HtmlContainer) Closure closure) {
        return new HtmlRow(application: this.application, parent: this)
            .tap { with(closure) }
            .tap {this.children.add(it) }
    }

    /**
     * Adds a new column container
     *
     * @param closure DSL of the content of this container
     * @return an instance of {@link HtmlColumn}
     * @since 0.1.0
     */
    HtmlColumn col(@DelegatesTo(HtmlContainer) Closure closure) {
        return new HtmlColumn(application: this.application, parent: this)
            .tap { with(closure) }
            .tap { this.children.add(it) }
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
            .tap { this.children.add(it) }
    }

    /**
     * Adds a new html button
     *
     * @param text text of the button
     * @param name name of the element
     * @param editable whether the element is editable or not
     * @param closure DSL of the content of this container
     * @return an instance of {@link HtmlButton}
     * @since 0.1.0
     */
    @NamedVariant
    HtmlButton button(
        @NamedParam String text,
        @NamedParam(required = false) String name = Utils.generateRandomName(),
        @NamedParam(required = false) boolean editable = true,
        @DelegatesTo(HtmlButton) Closure closure
    ){
        return new HtmlButton(application: this.application, name: name, text: text, editable: editable)
            .tap { with(closure) }
            .tap { this.children.add(it) }
            .tap { this.application.addElement(it) }
    }

    /**
     * Adds a new html button
     *
     * @param text text of the button
     * @param name name of the element
     * @param editable whether the element is editable or not
     * @return an instance of {@link HtmlButton}
     * @since 0.1.0
     */
    @NamedVariant
    HtmlButton button(
        @NamedParam String text,
        @NamedParam(required = false) String name = Utils.generateRandomName(),
        @NamedParam(required = false) boolean editable = true
    ){
        return new HtmlButton(application: this.application, name: name, text: text, editable: editable)
            .tap { this.children.add(it) }
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
        @NamedParam(required = false) boolean editable = true,
        @NamedParam(required = false) Number value = 0
    ) {
        return new HtmlInputNumber(
            application: this.application,
            label: label,
            name: name,
            editable: editable,
            value: value
        )
        .tap { this.children.add(it) }
        .tap { this.application.addElement(it) }
    }

    /**
     * Adds a new input text element
     *
     * @param name the name of the element
     * @param label label of the element
     * @param editable whether the element is editable or not
     * @return
     * @since 0.1.0
     */
    @NamedVariant
    HtmlInputText text(
        @NamedParam(required = false) String name = Utils.generateRandomName(),
        @NamedParam(required = false) String label = name,
        @NamedParam(required = false) boolean editable = true
    ) {
        return new HtmlInputText(application: this.application, name: name, label: label, editable: editable)
            .tap { this.children.add(it) }
            .tap { this.application.addElement(it) }
    }

    /**
     * Adds a new text area element
     *
     * @param name the name of the element
     * @param label label of the element
     * @param editable whether the element is editable or not
     * @return an instance of {@link HtmlTextArea}
     * @since 0.1.0
     */
    @NamedVariant
    HtmlTextArea textArea(
        @NamedParam(required = false) String name = Utils.generateRandomName(),
        @NamedParam(required = false) String label = name,
        @NamedParam(required = false) boolean editable = true
    ) {
        return new HtmlTextArea(application: this.application, name: name, label: label, editable: editable)
            .tap { this.children.add(it) }
            .tap { this.application.addElement(it) }
    }

    /**
     * Adds an element which shows a dataframe
     *
     * @param name the name of the element
     * @param label the label of the element
     * @param editable whether the element is editable or not
     * @return an instance of {@link HtmlDataFrame}
     * @since 0.1.0
     */
    @NamedVariant
    HtmlDataFrame dataframe(
            @NamedParam(required = false) String name = Utils.generateRandomName(),
            @NamedParam(required = false) String label = name,
            @NamedParam(required = false) boolean editable = true
    ) {
        return new HtmlDataFrame(application: this.application, name: name, label: label, editable: editable)
            .tap { this.children.add(it) }
            .tap { this.application.addElement(it) }
    }

    /**
     * Element rendering an Underdog's chart
     *
     * @param name the name of the elment
     * @param defaultValue default value to show the chart
     * @return an instance of {@link HtmlChart}
     * @since 0.1.0
     */
    @NamedVariant
    HtmlChart chart(
            @NamedParam(required = false) String name = Utils.generateRandomName(),
            @NamedParam(required = false) Object defaultValue = null,
            Closure<Options> supplier
    ) {
        return new HtmlChart(name: name, supplier: supplier, value: defaultValue)
            .tap { this.children.add(it) }
            .tap { this.application.addElement(it) }
    }

    /**
     * Represents a card showing a number with a title defining what that number means
     *
     * @param name the name of the component
     * @param title the title describing what the number means
     * @param defaultValue the default value when the component is rendered
     * @return an instance of {@link HtmlNumberCard}
     * @since 0.1.0
     */
    @NamedVariant
    HtmlNumberCard numberCard(
        @NamedParam(required = false) String name = Utils.generateRandomName(),
        @NamedParam(required = false) String title = "N/A",
        @NamedParam(required = false) Number defaultValue = 0
    ) {
        return new HtmlNumberCard(name: name, title: title, value: defaultValue)
            .tap { this.children.add(it) }
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
            .tap { this.children.add(it) }
            .tap { this.application.addElement(it) }
    }
}
