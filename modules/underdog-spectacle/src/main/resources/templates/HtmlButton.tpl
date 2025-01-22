def event = element.onClick

if (event) {
    button(
        'class': element.classNames('btn btn-outline-primary'),
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
        'class': element.classNames('btn btn-outline-primary mt-1'),
        'name': element.name
    ) {
        yield element.text
    }
}
