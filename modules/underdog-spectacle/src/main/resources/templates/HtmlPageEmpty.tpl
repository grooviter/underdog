div(class: 'empty') {
    div(class: 'empty-img'){
        img(src: '', height: 128){}
    }
    p(class: 'empty-title'){ yield "No results found" }
    p(class: 'empty-subtitle text-muted'){
        yield 'No elements added yet. Look in the documentation for inspiration'
    }
    div(class: 'empty-action') {
        a(href: '', class: 'btn btn-primary'){ yield 'Go to documentation' }
    }
}