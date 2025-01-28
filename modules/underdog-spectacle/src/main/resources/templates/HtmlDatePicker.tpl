div(class: 'mb-3'){
    if (element.label) {
        label(
            class: "form-label ${element.required ? 'required' : ''}",
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
    div(class: 'input-icon mt-2'){
        span(class: 'input-icon-addon'){
            i(class: "icon bi bi-calendar"){}
        }
        input(
            class: element.classNames('form-control'),
            type: 'date',
            name: element.name,
            required: element.required
        )
    }
}
