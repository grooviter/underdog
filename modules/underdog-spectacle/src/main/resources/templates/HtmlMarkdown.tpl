div(id: element.name, class: 'mb-3') {
    if (element.label) {
        label(for: element.name, class: 'form-label') {
            yield element.label
        }
    }
    if (element.info) {
        small(class: 'form-hint mb-2') {
            yield element.info
        }
    }
    yieldUnescaped markdown
}
