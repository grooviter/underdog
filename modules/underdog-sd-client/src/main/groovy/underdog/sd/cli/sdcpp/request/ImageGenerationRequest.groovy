package underdog.sd.cli.sdcpp.request

import com.fasterxml.jackson.annotation.JsonIgnoreProperties
import com.fasterxml.jackson.annotation.JsonInclude
import com.fasterxml.jackson.annotation.JsonProperty
import groovy.transform.builder.Builder
import underdog.sd.cli.Request

@Builder(includeSuperProperties = true)
@JsonIgnoreProperties(ignoreUnknown = true)
@JsonInclude(JsonInclude.Include.NON_NULL)
class ImageGenerationRequest extends CommonFields implements Request {
    @JsonProperty("ref_images")
    List<String> refImages

    @JsonProperty("mask_image")
    String maskImage

    @JsonProperty("control_image")
    String controlImage

    @JsonProperty("batch_count")
    Integer batchCount

    @JsonProperty("auto_resize_ref_image")
    Boolean autoResizeRefImage

    @JsonProperty("increase_ref_index")
    Boolean increaseRefIndex

    @JsonProperty("control_strength")
    Number controlStrength

    @JsonProperty("hires")
    HiRes hiRes
}
