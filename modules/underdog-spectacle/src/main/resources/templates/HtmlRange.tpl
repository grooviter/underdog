def showMarkers = element.showMarkers
def markersId = "${element.name}-markers"
def showUpdated = element.showUpdatedValue
def updatedValueId = "${element.name}-updated"

div(class: "row"){
    div(class: "col"){
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
    }
    if (showUpdated){
        div(
            id: updatedValueId,
            class: "col d-flex align-items-center justify-content-end form-label"){

        }
        script(type: 'text/javascript'){
            yieldUnescaped """
                document.addEventListener("load", (e) => {
                    const value = document.querySelector("#${updatedValueId}");
                    const input = document.querySelector("#${element.name}");
                    value.textContent = `\${input.value}${element.symbol}`;
                    input.addEventListener("input", (event) => {
                        value.textContent = `\${event.target.value}${element.symbol}`;
                    });
                }, true)
            """
        }
    }
}



div(class: 'row') {
    if (!showMarkers) {
        div(class: 'col-1 d-flex justify-content-center') {
            div(class:"col-auto align-self-center text-muted") {
                yield "${element.min}${element.symbol}"
            }
        }
    }
    div(class: 'col') {
        input(
            class: 'form-range',
            type: 'range',
            id: element.name,
            name: element.name,
            value: element.value,
            min: element.min,
            max: element.max,
            step: element.step,
            list: showMarkers ? markersId : ''
        )
        if (showMarkers) {
            datalist(id: markersId, class: 'text-muted') {
                (element.min..element.max).by(element.step).each { n->
                    option(value: n, label: n) {}
                }
            }
        }
    }
    if (!showMarkers) {
        div(class: 'col-1 d-flex justify-content-center') {
            div(class:"col-auto align-self-center text-muted") {
                yield "${element.max}${element.symbol}"
            }
        }
    }
}
