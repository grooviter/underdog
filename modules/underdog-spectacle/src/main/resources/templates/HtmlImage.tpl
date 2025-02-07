div(id: element.name, class: 'mb-3') {
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
    if (element.value) {
        div(class: 'card') {
            div(class: 'card-body d-flex justify-content-center') {
                img(src: element.value, class: element.className){}
            }
        }
    } else {
        div(class: 'card'){
            div(class: 'empty') {
                div(class: 'empty-image') {
                    img(class: 'w-50', src: 'static/images/empty_image.svg'){}
                }
                p(class: 'empty-title'){ yield 'Image' }
                p(class: 'empty-subtitle text-muted') {
                    yield 'This component will show a picture as soon as it gets one'
                }
            }
        }
    }
}