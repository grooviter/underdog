class SPCLogger {
    constructor(id) {
        this.id = id
    }

    debug(message) {
        console.log(`${new Date().toISOString()} - ${this.id} - DEBUG - ${message}`)
    }
}


function loadScript(
    src,
    integrity,
    crossOrigin,
    referrerPolicy
) {
    return new Promise(function(resolve, reject) {
        let scripts = document.getElementsByTagName('script')
        for (const script of scripts) {
            if (script.attributes.src === src) {
                resolve()
            }
        }

        var script = document.createElement('script');

        if (script.src == null) {
            reject("missing script src attribute");
        }

        script.src = src;

        if (integrity) {
            script.integrity = integrity;
        }

        if (crossOrigin) {
            script.crossOrigin = crossOrigin;
        }

        if (referrerPolicy) {
            script.referrerPolicy = referrerPolicy;
        }

        script.onload = function () {
            resolve();
        };
        script.onerror = function () {
            reject();
        };

        document.body.appendChild(script);
    })
}