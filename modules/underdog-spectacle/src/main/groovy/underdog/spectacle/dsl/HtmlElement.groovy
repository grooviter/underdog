package underdog.spectacle.dsl

abstract class HtmlElement {
    String name
    Boolean editable
    List<HtmlEvent> eventList = []
}
