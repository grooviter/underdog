div(class: 'mb-3') {
    if (element.label) {
        label(class: 'form-label', for: element.name) {
            yield element.label
        }
    }
    if (element.info) {
        small(class: 'form-hint') {
            yield element.info
        }
    }
    input(
        class: 'form-control',
        type: 'number',
        name: element.name,
        value: element.value
    )
}
