package underdog.sd.cli.sdcpp

import com.fasterxml.jackson.annotation.JsonProperty
import groovy.transform.builder.Builder
import underdog.sd.cli.HiResUpscaler
import underdog.sd.cli.Txt2ImageBaseOptions

@Builder(includeSuperProperties = true)
class ImageGenerationOptions extends Txt2ImageBaseOptions {
    @JsonProperty(value  = "ref_images", defaultValue = "[]")
    List<String> refImages = []

    Number hiResScale = 2
    Boolean hiResEnabled = false
    HiResUpscaler hiResUpscaler = HiResUpscaler.ESRGAN_4x
    Number strength = 0.1
}
