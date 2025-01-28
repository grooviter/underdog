def event = element.onSubmit
def target = event.outputList.find()

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
