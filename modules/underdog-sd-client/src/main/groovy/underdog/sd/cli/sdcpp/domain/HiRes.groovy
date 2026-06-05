package underdog.sd.cli.sdcpp.domain

import com.fasterxml.jackson.annotation.JsonProperty

class HiRes {
    @JsonProperty("custom_sigmas")
    List<String> customSigmas

    @JsonProperty("denoising_strength")
    Double denoisingStrength

    @JsonProperty("enabled")
    Boolean enabled

    @JsonProperty("scale")
    Double scale

    @JsonProperty("steps")
    Integer steps

    @JsonProperty("target_height")
    Integer targetHeight

    @JsonProperty("target_width")
    Integer targetWidth

    @JsonProperty("upscale_tile_size")
    Integer upscaleTileSize

    @JsonProperty("upscaler")
    String upscaler
}
