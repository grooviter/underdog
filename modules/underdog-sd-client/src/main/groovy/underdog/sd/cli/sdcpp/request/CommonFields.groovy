package underdog.sd.cli.sdcpp.request

import com.fasterxml.jackson.annotation.JsonProperty

class CommonFields {
    @JsonProperty("prompt")
    String prompt

    @JsonProperty("negative_prompt")
    String negativePrompt

    @JsonProperty("init_image")
    String initImage

    @JsonProperty("cfg_scale")
    Number cfgScale

    @JsonProperty("clip_skip")
    Integer clipSkip

    @JsonProperty("width")
    Integer width

    @JsonProperty("height")
    Integer height

    @JsonProperty("strength")
    Number strength

    @JsonProperty("seed")
    Integer seed

    @JsonProperty("sample_params")
    SampleParams sampleParams

    @JsonProperty("vae_tiling_params")
    VaeTilingParams vaeTilingParams

    @JsonProperty("cache_mode")
    String cacheMode

    @JsonProperty("cache_option")
    String cacheOption

    @JsonProperty("scm_mask")
    String scmMask

    @JsonProperty("scm_policy_dynamic")
    Boolean scmPolicyDynamic

    @JsonProperty("output_format")
    String outputFormat

    @JsonProperty("output_compression")
    Integer outputCompression
}
