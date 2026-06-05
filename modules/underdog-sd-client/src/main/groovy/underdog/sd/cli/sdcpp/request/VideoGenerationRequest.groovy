package underdog.sd.cli.sdcpp.request

import com.fasterxml.jackson.annotation.JsonProperty
import groovy.transform.builder.Builder
import underdog.sd.cli.Request

@Builder(includeSuperProperties = true)
class VideoGenerationRequest extends CommonFields implements Request {
    @JsonProperty("video_frames")
    Integer videoFrames

    @JsonProperty("fps")
    Integer fps

    @JsonProperty("moe_boundary")
    Number moeBoundary

    @JsonProperty("vace_strength")
    Number vaceStrength

    @JsonProperty("high_noise_sample_params")
    SampleParams highNoiseSampleParams
}
