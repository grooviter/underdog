div(class: 'mb-3') {
    if (element.label) {
        label(for: element.name, class: 'form-label') {
            yield element.label
        }
    }
    if (element.info) {
        small(class: 'form-hint') {
            yield element.info
        }
    }
    textArea(
        id: element.name,
        name: element.name,
        class: 'form-control',
        rows: element.rows
    ){
        yield element.value
    }
}

