package underdog.sd.cli.sdcpp.domain

import com.fasterxml.jackson.annotation.JsonProperty

class Features {
    @JsonProperty("cache")
    Boolean cache

    @JsonProperty("cancel_generating")
    Boolean cancelGenerating

    @JsonProperty("cancel_queued")
    Boolean cancelQueued

    @JsonProperty("control_image")
    Boolean controlImage

    @JsonProperty("hires")
    Boolean hires

    @JsonProperty("init_image")
    Boolean initImage

    @JsonProperty("lora")
    Boolean lora

    @JsonProperty("mask_image")
    Boolean maskImage

    @JsonProperty("ref_images")
    Boolean refImages

    @JsonProperty("vae_tiling")
    Boolean vaeTiling
}
