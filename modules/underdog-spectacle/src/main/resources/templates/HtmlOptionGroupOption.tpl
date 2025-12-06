label(class: 'form-check'){
    if (element.checked) {
        input(
            class: 'form-check-input',
            type: 'radio',
            name: element.name,
            value: element.value,
            checked: true
        ){}
    } else {
        input(
            class: 'form-check-input',
            type: 'radio',
            value: element.value,
            name: element.name
        ){}
    }

    span(class: 'form-check-label') {
        yield element.value
    }
    if (element.info) {
        small(class: 'form-hint mb-2') {
            yield element.info
        }
    }
}