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
        small(class: 'form-hint mb-2') {
            yield element.info
        }
    }
    div(class: 'input-icon mt-2'){
        span(class: 'input-icon-addon'){
            i(class: "icon bi bi-clock-history"){}
        }
        input(
                class: element.classNames('form-control'),
                type: 'time',
                name: element.name,
                required: element.required
        )
    }
}
