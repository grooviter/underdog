div(class: 'input-group') {
    if (element.prefix){
        span(class: 'input-group-text'){
            yield element.prefix
        }
    }
    include template: "templates/HtmlInputText/HtmlInputTextBase.tpl"
    if (element.suffix) {
        span(class: 'input-group-text'){
            yield element.suffix
        }
    }
}