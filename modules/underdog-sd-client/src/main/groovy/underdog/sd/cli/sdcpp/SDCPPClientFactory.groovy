package underdog.sd.cli.sdcpp


import underdog.sd.cli.ApiOptions
import underdog.sd.cli.http.HTTPService

class SDCPPClientFactory {
    static SDCPPClient create(ApiOptions apiOptions){
        return new SDCPPClientImplementation(HTTPService.defaults(apiOptions))
    }
}
