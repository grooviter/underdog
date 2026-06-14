package underdog.spectacle.dsl

class HtmlNavigationGroup extends HtmlElement {
    String icon

    String title

    List<HtmlPage> included = []

    void addPage(HtmlPage page){
        this.included.add(page)
    }
}
