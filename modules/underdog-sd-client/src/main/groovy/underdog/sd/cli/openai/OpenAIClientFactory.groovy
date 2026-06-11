package underdog.sd.cli.openai

import underdog.sd.cli.ApiOptions
import underdog.sd.cli.http.HTTPService

class OpenAIClientFactory {
    static OpenAIClient create(ApiOptions apiOptions) {
        String envBaseURL = System.getenv('OPENAI_API_BASE_URL')
        String envApiKey = System.getenv('OPENAI_API_KEY')

        ApiOptions merged = ApiOptions.builder()
            .baseUrl(apiOptions.baseUrl ?: envBaseURL)
            .apiKey(apiOptions.apiKey ?: envApiKey)
            .build()

        return new OpenAIClientImplementation(HTTPService.defaults(merged))
    }
}
