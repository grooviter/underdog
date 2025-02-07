def displayClass = element.display ? "" : "d-none"

div(
    id: element.name,
    class: element.classNames("progress progress-sm $displayClass")
) {
    div(class: 'progress-bar progress-bar-indeterminate'){}
}