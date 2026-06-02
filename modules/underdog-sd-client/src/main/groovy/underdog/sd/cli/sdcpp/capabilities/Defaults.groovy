package underdog.sd.cli.sdcpp.capabilities

import com.fasterxml.jackson.annotation.JsonProperty

class Defaults {
    @JsonProperty("auto_resize_ref_image")
    String autoResizeRefImage

    @JsonProperty("batch_count")
    Integer batchCount

    @JsonProperty("cache_mode")
    String cacheMode

    @JsonProperty("cache_option")
    String cacheOption

    @JsonProperty("clip_skip")
    Integer clipSkip

    @JsonProperty("control_strength")
    Double controlStrength

    @JsonProperty("height")
    Integer height

    @JsonProperty("hires")
    HiRes hiRes

    @JsonProperty("increase_ref_index")
    Boolean increaseRefIndex

    @JsonProperty("negative_prompt")
    String negativePrompt

    @JsonProperty("output_compression")
    Integer outputCompression

    @JsonProperty("output_format")
    String outputFormat

    @JsonProperty("prompt")
    String prompt

    @JsonProperty("sample_params")
    SampleParams sampleParams

    @JsonProperty("scm_mask")
    String scmMask

    @JsonProperty("scm_policy_dynamic")
    Boolean scmPolicyDynamic

    @JsonProperty("seed")
    Integer seed

    @JsonProperty("strength")
    Double strength

    @JsonProperty("vae_tiling_params")
    VaeTilingParams vaeTilingParams

    @JsonProperty("width")
    Integer width
}
