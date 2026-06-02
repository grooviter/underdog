package underdog.sd.cli.openai

import groovy.transform.TupleConstructor
import underdog.sd.cli.http.HTTPService

@TupleConstructor
class OpenAIClientImplementation implements OpenAIClient {
    HTTPService httpService

    @Override
    ImagesResult imageGeneration(GenerationsOptions options) {
        return httpService.executePOST('/v1/images/generations', options, ImagesResult)
    }
}
