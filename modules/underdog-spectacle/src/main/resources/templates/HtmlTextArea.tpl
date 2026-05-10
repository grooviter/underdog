def requiredAttributeMap = element.required ? [required: element.required] : [:]

div(id: element.name, class: 'mb-3') {
    if (element.label) {
        label(for: element.name, class: "form-label ${element.required ? 'required' : ''}") {
            yield element.label
        }
    }
    if (element.info) {
        small(class: 'form-hint mb-2') {
            yield element.info
        }
    }
    textArea(
        *: requiredAttributeMap,
        name: element.name,
        class: 'form-control',
        rows: element.rows,
    ){
        yield element.value
    }
}

