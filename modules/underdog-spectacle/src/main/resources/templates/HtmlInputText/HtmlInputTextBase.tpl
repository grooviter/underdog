def event = element.onEnter

def mandatoryAttributes = [
    class: element.classNames('form-control'),
    placeholder: element.placeHolder,
    type: 'text',
    name: element.name,
    required: element.required,
    value: element.value
]

if (event) {
    def eventAttributes = [
        'hx-post': event.path,
        'hx-target': "#${event.outputList.find()}",
        'hx-trigger': 'keyup[keyCode==13]', // when pressing enter
    ]

    input(mandatoryAttributes + eventAttributes)
} else {
    input(mandatoryAttributes)
}