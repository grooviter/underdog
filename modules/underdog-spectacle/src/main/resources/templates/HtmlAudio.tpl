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
        small(class: 'form-hint mb-2') {
            yield element.info
        }
    }
    if (element.value) {
        audio(class: "w-100", controls: "", src: element.value){}
    } else {
        audio(class: "w-100", controls: "", style: 'opacity: .25; pointer-events: none'){}
    }
}
