div(id: element.name, class: 'mb-3'){
    if (element.label) {
        label(
            class: 'form-label',
            for: element.label
        ) {
            yield element.label
        }
    }
    if (element.info) {
        small(class: 'form-hint mb-2') {
            yield element.info
        }
    }
    select(
        name: element.name,
        class: element.classNames('form-select mt-1')
    ) {
        yieldUnescaped childrenContent
    }
}

