package underdog.spectacle.dsl

abstract class HtmlElement {
    String name = Utils.generateRandomName()
    String label
    Boolean editable
    List<HtmlEvent> eventList = []
}
