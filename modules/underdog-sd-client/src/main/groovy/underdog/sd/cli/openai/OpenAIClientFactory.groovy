package underdog.sd.cli.openai

import underdog.sd.cli.ApiOptions
import underdog.sd.cli.http.HTTPService

class OpenAIClientFactory {
    static OpenAIClient create(ApiOptions apiOptions) {
        return new OpenAIClientImplementation(HTTPService.defaults(apiOptions))
    }
}
