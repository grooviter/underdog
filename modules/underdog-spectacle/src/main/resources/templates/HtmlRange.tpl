if (element.label) {
    label(class: 'form-label', for: element.name) {
        yield element.label
    }
}
input(
    class: 'form-range',
    type: 'range',
    name: element.name,
    value: element.value
)
