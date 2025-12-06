import { loadScript } from "/static/js/spc-utils.js";

function loadChart(container_id, options) {
    loadScript(
        'https://cdnjs.cloudflare.com/ajax/libs/echarts/5.5.1-rc.1/echarts.min.js',
        'sha512-RaatU6zYCjTIyGwrBVsUzvbkpZq/ECxa2oYoQ+16qhQDCX9xQUEAsm437n/6TNTes94flHU4cr+ur99FKM5Vog==',
        'anonymous',
        'no-referrer'
    ).then(function() {
        echarts
            .init(document.getElementById(container_id))
            .setOption(options);
    });
}

export { loadChart };