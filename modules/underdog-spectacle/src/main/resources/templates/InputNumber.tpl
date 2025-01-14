if (element.label) {
    label(for: element.name) { yield element.label }
}
input(type: 'number', name: element.name)