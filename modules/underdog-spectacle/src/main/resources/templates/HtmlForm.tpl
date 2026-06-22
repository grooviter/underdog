def event = element.onSubmit
def target = event.outputList.find()

if (element.isStreaming()) {
    form(
        'class': 'pf-v6-c-form',
        'id': element.name,
        'name': element.name,
        'ws-send': 'true',
        'hx-swap': 'outerHTML',
        'hx-indicator': element.indicatorSelector,
        'hx-disabled-elt': element.disabledSelector
    ) {
        script(type: 'module') {
            yieldUnescaped """\
                | import { HtmlFormStreaming } from "/static/js/spc-form.js";
                |
                | new HtmlFormStreaming().init();
            """.stripMargin().stripIndent()
        }

        yieldUnescaped childrenContent
    }
} else {
    form(
        'class': 'pf-v6-c-form',
        'id': element.name,
        'name': element.name,
        'hx-post': event.path,
        'hx-target': "#${target}",
        'hx-swap': 'outerHTML',
        'hx-indicator': element.indicatorSelector,
        'hx-disabled-elt': element.disabledSelector
    ) {
        yieldUnescaped childrenContent
    }
}
