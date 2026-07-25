package underdog.sd.cli.common

import spock.lang.Specification
import underdog.sd.cli.SD
import underdog.sd.cli.openai.OpenAIClient
import underdog.sd.cli.sdapi.SDAPIClient
import underdog.sd.cli.Images
import underdog.sd.cli.sdcpp.SDCPPClient

class SDAwareSpec extends Specification {

    SDAPIClient getSdapi() {
        return SD.sdapi()
    }

    SDCPPClient getSdcpp() {
        return SD.sdcpp()
    }

    OpenAIClient getOpenAI() {
        return SD.openai()
    }

    String getAlfredHitchcockBase64Image() {
        return loadImageAsBase64("alfred_hitchcock_by_jack_mitchell.jpg")
    }

    String getArnoldSchwarzeneggerBase64Image() {
        return loadImageAsBase64("arnold_schwarzenegger.jpg")
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
