
    if (element.label) {
        label(class: 'form-label', for: element.name) {
            yield element.label
        }
    }
    input(
        class: 'form-control',
        type: 'number',
        name: element.name,
        value: element.value
    )
