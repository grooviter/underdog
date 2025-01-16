def event = element.onClick

if (event) {
    button(
        'class': 'btn btn-outline-primary',
        'name': element.name,
        'hx-post': event.path,
        'hx-trigger': "click",
        'hx-target': "#${event.outputList.find()}",
        'hx-swap': 'outerHTML'
    ) {
        yield element.text
    }
} else {
    button(
        class: 'btn btn-outline-primary',
        name: element.name
    ) {
        yield element.text
    }
}
