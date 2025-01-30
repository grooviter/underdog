div(
    class: 'chart mb-3',
    id: element.name,
    name: element.name
) {
    if (element.label) {
        label(
            class: 'form-label',
            for: element.label
        ) {
            yield element.label
        }
    }
    if (element.info) {
        small(class: 'form-hint') {
            yield element.info
        }
    }
    div(id: "${element.name}-container", class: 'w-100 h-100') {}
    script(type: 'module') {
        yieldUnescaped """\
            | import { loadChart } from "/static/js/spc-charts.js";
            | 
            | loadChart('${element.name}-container', ${element.chartAsString});
        """.stripMargin().stripIndent()
    }
}