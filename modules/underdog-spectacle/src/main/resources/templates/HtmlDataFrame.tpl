// DATA
def data = element?.value
def cols = data?.columns
def rows = data?.toList()

// HTML
table(
    id: element.name,
    name: element.name
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
    tfoot {

    }
}