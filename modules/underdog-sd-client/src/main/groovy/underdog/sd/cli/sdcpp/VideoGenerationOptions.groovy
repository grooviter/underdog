package underdog.sd.cli.sdcpp

import com.fasterxml.jackson.annotation.JsonProperty
import groovy.transform.builder.Builder
import underdog.sd.cli.Options
import underdog.sd.cli.sdcpp.domain.SampleParams

@Builder
class VideoGenerationOptions implements Options {
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

    @JsonProperty("strength")
    Number strength

    @JsonProperty("width")
    Integer width

    @JsonProperty("height")
    Integer height

    @JsonProperty("seed")
    Integer seed

    @JsonProperty("video_frames")
    Integer videoFrames

    @JsonProperty("fps")
    Integer fps

    @JsonProperty("sample_params")
    SampleParams sampleParams
}
