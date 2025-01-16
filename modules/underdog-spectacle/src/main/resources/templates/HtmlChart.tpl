div(
    class: 'chart',
    id: element.name,
    name: element.name
) {
    div(id: 'display-container', class: 'w-100 h-100') {}
    script(type: 'text/javascript') {
        yieldUnescaped """\
           | loadScript(
           |     'https://cdnjs.cloudflare.com/ajax/libs/echarts/5.5.1-rc.1/echarts.min.js',
           |     'sha512-RaatU6zYCjTIyGwrBVsUzvbkpZq/ECxa2oYoQ+16qhQDCX9xQUEAsm437n/6TNTes94flHU4cr+ur99FKM5Vog==',
           |     'anonymous',
           |     'no-referrer'
           | ).then(function() {
           |     var chart = echarts.init(document.getElementById("display-container"));
           |     var option = ${element.chartAsString};
           |     chart.setOption(option);
           | })
        """.stripMargin().stripIndent()
    }
}