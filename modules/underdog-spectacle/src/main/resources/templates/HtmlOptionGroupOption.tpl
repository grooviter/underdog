label(class: 'form-check'){
    if (element.checked) {
        input(class: 'form-check-input', type: 'radio', name: element.name, checked: true){}
    } else {
        input(class: 'form-check-input', type: 'radio', name: element.name){}
    }

    span(class: 'form-check-label') {
        yield element.value
    }
    if (element.info) {
        small(class: 'form-hint') {
            yield element.info
        }
    }
}