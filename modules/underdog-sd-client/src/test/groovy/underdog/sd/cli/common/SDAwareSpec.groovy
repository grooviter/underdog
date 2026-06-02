package underdog.sd.cli.common

import spock.lang.Specification
import underdog.sd.cli.ApiOptions
import underdog.sd.cli.SD
import underdog.sd.cli.openai.OpenAIClient
import underdog.sd.cli.sdapi.SDAPIClient
import underdog.sd.cli.Images
import underdog.sd.cli.sdcpp.SDCPPClient

class SDAwareSpec extends Specification {

    SDAPIClient getSdapi() {
        return SD.sdapi(ApiOptions.builder()
            .baseUrl("https://sdcpp.lab.bit2lab.com")
            .build())
    }

    SDCPPClient getSdcpp() {
        return SD.sdcpp(ApiOptions.builder()
            .baseUrl("https://sdcpp.lab.bit2lab.com")
            .build())
    }

    OpenAIClient getOpenAI() {
        return SD.openai(ApiOptions.builder()
            .baseUrl("https://vllm.lab.bit2lab.com")
            .apiKey("Y7Ymm9poIq4QZdFCyCnzZA==")
            .build())
    }

    String getAlfredHitchcockBase64Image() {
        return loadImageAsBase64("alfred_hitchcock_by_jack_mitchell.jpg")
    }

    String getPuertaDeAlcalaBase64Image() {
        return loadImageAsBase64("puerta_de_alcala_madrid_2.jpg")
    }

    String getPuertaDeAlcalaMaskBase64Image() {
        return loadImageAsBase64("puerta_de_alcala_madrid_2_mask.jpg")
    }

    private static String loadImageAsBase64(String name) {
        return Images.inputStreamToBase64(SDAwareSpec.classLoader.getResourceAsStream(name))
    }
}
