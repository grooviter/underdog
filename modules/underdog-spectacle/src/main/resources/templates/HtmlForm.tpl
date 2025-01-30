def event = element.onSubmit
def target = event.outputList.find()

if (element.isStreaming()) {
    form(
        'class': 'pf-v6-c-form',
        'id': element.name,
        'name': element.name,
        'ws-send': 'true'
    ) {
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
        'hx-indicator': element.indicatorSelector
    ) {
        yieldUnescaped childrenContent
    }
}
