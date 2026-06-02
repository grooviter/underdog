package underdog.sd.cli.sdapi


import underdog.sd.cli.ApiOptions
import underdog.sd.cli.http.HTTPService

class SDAPIClientFactory {
    static SDAPIClient create(ApiOptions apiOptions) {
        return new SDAPIClientImplementation(HTTPService.defaults(apiOptions))
    }
}
