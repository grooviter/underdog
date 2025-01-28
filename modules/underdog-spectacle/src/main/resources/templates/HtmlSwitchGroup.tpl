div(class: 'mb-3') {
    if (element.label){
        div(class: 'form-label'){
            yield element.label
        }
    }
    if (element.info) {
        small(class: 'form-hint') {
            yield element.info
        }
    }
    div(class: 'form-switch px-3 mt-2') {
        yieldUnescaped childrenContent
    }
}