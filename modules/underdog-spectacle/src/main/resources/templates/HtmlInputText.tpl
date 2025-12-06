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
    if (element.icon) {
        include template: "templates/HtmlInputText/HtmlInputTextWithIcon.tpl"
    } else if (element.suffix || element.prefix) {
        include template: "templates/HtmlInputText/HtmlInputTextWithPrefixAndSuffix.tpl"
    } else {
        include template: "templates/HtmlInputText/HtmlInputTextBase.tpl"
    }
}
