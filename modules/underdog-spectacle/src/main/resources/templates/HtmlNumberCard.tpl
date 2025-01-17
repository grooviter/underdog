div(id: element.name, class: 'card') {
    div(class: 'card-body') {
        h5(class: 'card-title') { yield element.title }
        p(class: 'card-text') {
            yield element.value
        }
    }
}