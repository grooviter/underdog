def event = element.onClick
def icon = element.icon

if (event) {
    button(
        'class': element.classNames('btn btn-outline-primary'),
        'id': element.name,
        'name': element.name,
        'hx-post': event.path,
        'hx-trigger': "click",
        'hx-target': "#${event.outputList.find()}",
        'hx-swap': 'outerHTML'
    ) {
        if (icon) {
            i(class: "icon $icon"){}
        }
        yield element.text
    }
} else {
    button(
        'class': element.classNames('btn btn-outline-primary mt-1'),
        'id': element.name,
        'name': element.name
    ) {
        if (icon) {
            i(class: "icon $icon"){}
        }
        yield element.text
    }
}
