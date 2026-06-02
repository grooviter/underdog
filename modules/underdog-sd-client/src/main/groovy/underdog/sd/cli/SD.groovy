package underdog.sd.cli

import underdog.sd.cli.openai.OpenAIClient
import underdog.sd.cli.openai.OpenAIClientFactory
import underdog.sd.cli.sdapi.SDAPIClient
import underdog.sd.cli.sdapi.SDAPIClientFactory
import underdog.sd.cli.sdcpp.SDCPPClient
import underdog.sd.cli.sdcpp.SDCPPClientFactory

/**
 *
 * @since 0.1.0
 */
class SD {

    /**
     * @param apiOptions
     * @return
     * @since 0.1.0
     */
    static SDCPPClient sdcpp(ApiOptions apiOptions) {
        return SDCPPClientFactory.create(apiOptions)
    }

    /**
     * @param apiOptions
     * @return
     * @since 0.1.0
     */
    static SDAPIClient sdapi(ApiOptions apiOptions) {
        return SDAPIClientFactory.create(apiOptions)
    }

    /**
     * @param apiOptions
     * @return
     * @since 0.1.0
     */
    static OpenAIClient openai(ApiOptions apiOptions) {
        return OpenAIClientFactory.create(apiOptions)
    }
}
