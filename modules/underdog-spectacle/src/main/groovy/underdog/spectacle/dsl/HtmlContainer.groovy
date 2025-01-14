package underdog.spectacle.dsl

import groovy.transform.NamedParam
import groovy.transform.NamedVariant

abstract class HtmlContainer extends HtmlElement {
    HtmlContainer parent
    List<HtmlElement> children = []

    Row row(@DelegatesTo(HtmlContainer) Closure closure) {
        return new Row(parent: this)
            .tap { with(closure) }
            .tap {this.children.add(it) }
    }

    Column col(@DelegatesTo(HtmlContainer) Closure closure) {
        return new Column(parent: this)
            .tap { with(closure) }
            .tap { this.children.add(it) }
    }

    @NamedVariant
    Button button(
        @NamedParam String text,
        @NamedParam(required = false) String name = Utils.generateRandomName(),
        @NamedParam(required = false) boolean editable = true,
        @DelegatesTo(Button) Closure closure
    ){
        return new Button(name: name, text: text, editable: editable)
            .tap { with(closure) }
            .tap { this.children.add(it) }
    }

    @NamedVariant
    Button button(
        @NamedParam String text,
        @NamedParam(required = false) String name = Utils.generateRandomName(),
        @NamedParam(required = false) boolean editable = true
    ){
        return new Button(name: name, text: text, editable: editable).tap(this.children::add)
    }

    @NamedVariant
    InputNumber number(
        @NamedParam(required = false) String name = Utils.generateRandomName(),
        @NamedParam(required = false) String label = name,
        @NamedParam(required = false) boolean editable = true
    ) {
        return new InputNumber(label: label, name: name, editable: editable).tap(this.children::add)
    }

    @NamedVariant
    InputText text(
        @NamedParam(required = false) String name = Utils.generateRandomName(),
        @NamedParam(required = false) String label = name,
        @NamedParam(required = false) boolean editable = true
    ) {
        return new InputText(name: name, label: label, editable: editable).tap { this.children.add(it) }
    }

    @NamedVariant
    TextArea textArea(
        @NamedParam(required = false) String name = Utils.generateRandomName(),
        @NamedParam(required = false) String label = name,
        @NamedParam(required = false) boolean editable = true
    ) {
        return new TextArea(name: name, label: label, editable: editable).tap(this.children::add)
    }

    Markdown markdown(String markdown) {
        return new Markdown(markdown).tap(this.children::add)
    }
}
