div(id: element.name, class: 'mb-3'){
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
    if (element.icon) {
        div(class: 'input-icon'){
            span(class: 'input-icon-addon'){
                i(class: "icon ${element.icon}"){}
            }
            input(
                class: element.classNames('form-control'),
                placeholder: element.placeHolder,
                type: 'text',
                name: element.name,
                required: element.required,
                value: element.value
            )
        }
    } else if (element.suffix || element.prefix) {
        div(class: 'input-group') {
            if (element.prefix){
                span(class: 'input-group-text'){
                    yield element.prefix
                }
            }
            input(
                class: element.classNames('form-control'),
                placeholder: element.placeHolder,
                type: 'text',
                name: element.name,
                required: element.required,
                value: element.value
            )
            if (element.suffix) {
                span(class: 'input-group-text'){
                    yield element.suffix
                }
            }
        }
    } else {
        input(
            class: element.classNames('form-control'),
            placeholder: element.placeHolder,
            type: 'text',
            name: element.name,
            required: element.required,
            value: element.value
        )
    }
}
