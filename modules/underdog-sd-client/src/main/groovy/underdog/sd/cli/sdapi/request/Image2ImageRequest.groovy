package underdog.sd.cli.sdapi.request

import com.fasterxml.jackson.annotation.JsonIgnoreProperties
import com.fasterxml.jackson.annotation.JsonInclude
import com.fasterxml.jackson.annotation.JsonProperty
import groovy.transform.builder.Builder

@Builder(includeSuperProperties = true)
@JsonIgnoreProperties(ignoreUnknown = true)
@JsonInclude(JsonInclude.Include.NON_NULL)
class Image2ImageRequest extends Txt2ImageRequest {
    @JsonProperty("init_images")
    List<String> initImages

    @JsonProperty("mask")
    String mask

    @JsonProperty("inpainting_mask_invert")
    Boolean inpaintingMaskInvert
}
