label(class: 'form-check'){
    if (element.checked) {
        input(
            class: 'form-check-input',
            type: 'checkbox',
            name: element.name,
            checked: true){}
    } else {
        input(
            class: 'form-check-input',
            type: 'checkbox',
            value: element.value,
            name: element.name){}
    }
    span(class: 'form-check-label') {
        yield element.label
    }
}