const TIMEOUT_DEFAULT = 2000

class WSConnection {
    constructor() {
        this.logger = new SPCLogger('scp-utils.WSConnection')
    }

    init() {
        try {
            this.ws = new WebSocket(this.connectionUrl)
            this.ws.addEventListener("open", (event) => this.onOpenExecution({ event, conn: this }))
            this.ws.addEventListener("close", (event) => this.onCloseExecution({ event, conn: this }))
            this.ws.addEventListener("message", (event) => this.onMessageExecution({ event, conn: this }))
        } catch(e) {
            setTimeout(() => this.init(), TIMEOUT_DEFAULT)
        }
    }

    url(url) {
        this.connectionUrl = url
        return this
    }

    onOpen(execution) {
        this.logger.debug('setting onOpen function')
        this.onOpenExecution = execution
        return this
    }

    onMessage(execution) {
        this.logger.debug('setting onMessage function')
        this.onMessageExecution = execution
        return this
    }

    onClose(execution) {
        this.logger.debug('setting onClose function')
        this.onCloseExecution = execution
        return this
    }

    startPinging() {
        setInterval(() => {
            new Promise((resolve, reject) => {
                try {
                    this.logger.debug("sending ping")
                    this.ws.send('PING')
                } catch(e) {
                    reject(e)
                }
            }).then(() => {
                this.tm = setTimeout(() => this.close(), TIMEOUT_DEFAULT * 2)
            }).catch((error) => {
                this.stopClosingProcess()
                init()
            })
        }, TIMEOUT_DEFAULT)
    }

    stopClosingProcess() {
        if (this.tm) {
            clearTimeout(this.tm)
        }
    }

    ping() {
        this.logger.debug("sending ping")
        this.ws.send('PING')
        this.tm = setTimeout(() => this.close(), TIMEOUT_DEFAULT * 2)
    }

    pong(version) {
        this.logger.debug("pong received")
        this.logger.debug(`client: ${this.version} -- server: ${version}`)
        if (!this.version) {
            this.logger.debug(`skipping closing and updating version`)
            this.stopClosingProcess()
            this.version = version
        } else if (this.version == version) {
            this.logger.debug(`just skipping closing`)
            this.stopClosingProcess()
        } else {
            this.logger.debug(`reloading`)
            location.reload()
        }
    }

    reconnect(timeout) {
        setTimeout(() => {
            this.logger.debug("reconnecting")
            this.init()
        }, timeout)
    }

    close(event) {
        this.logger.debug(`socket close, reason: ${event.reason}`)
        this.ws.close()
    }
}

new WSConnection()
    .url("ws://localhost:5000/ws/status")
    .onOpen(({ event, conn }) => {
        conn.startPinging()
    })
    .onMessage(({ event, conn }) => {
        const json = JSON.parse(event.data)
        if (json.type == 'PONG') {
            conn.pong(json.version)
        }
    })
    .onClose(({ event, conn }) => {
        conn.close(event)
        conn.reconnect(TIMEOUT_DEFAULT)
    })
    .init()