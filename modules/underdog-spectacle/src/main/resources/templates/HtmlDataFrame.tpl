def data = element?.value
def cols = data?.columns
def rows = data?.toList()

div(class: 'mb-3', id: element.name){
    if (element.label) {
        label(class: 'form-label', for: element.name) {
            yield element.label
        }
    }
    if (element.info) {
        small(class: 'form-hint mb-2') {
            yield element.info
        }
    }
    div(class: 'card') {
        if (!element?.value?.size()) {
            include template: 'templates/HtmlDataFrameEmpty.tpl'
        } else {
            table(
                name: element.name,
                style: 'font-size: 0.9em',
                class: element.className ?: 'table card-table table-vcenter'
            ) {
                thead {
                    cols?.each {
                        th(it)
                    }
                }
                tbody {
                    rows.each { List row ->
                        tr {
                            (0..<cols.size()).each { col ->
                                td(row.get(col))
                            }
                        }
                    }
                }
            }
        }

    }
}


