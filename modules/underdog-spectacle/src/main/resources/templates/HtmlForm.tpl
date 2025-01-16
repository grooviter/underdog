def event = element.onSubmit

form(
    'class': 'pf-v6-c-form',
    'name': element.name,
    'hx-post': event.path,
    'hx-target': "#${event.outputList.find()}",
    'hx-swap': 'outerHTML'
) {
    yieldUnescaped childrenContent
}