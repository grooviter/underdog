div(class: 'mb-3'){
    if (element.label) {
        label(
                class: 'form-label',
                for: element.label
        ) {
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
            placeholder: element.placeHolder,
            type: 'text',
            name: element.name
    )
}
