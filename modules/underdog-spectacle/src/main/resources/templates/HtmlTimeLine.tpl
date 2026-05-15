ul(id: element.name, class: "list list-timeline", 'hx-swap-oob': 'true') {
    if (element.value) {
       element.value.items.each { item ->
            element = item
            include template: "templates/HtmlTimeLineItem.tpl"
       }
    } else {
        yieldUnescaped childrenContent
    }
}
