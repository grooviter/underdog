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
    select(class: 'form-select mt-1') {
        yieldUnescaped childrenContent
    }
}

