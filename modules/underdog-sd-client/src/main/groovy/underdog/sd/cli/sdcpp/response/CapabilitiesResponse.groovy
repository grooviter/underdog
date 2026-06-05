package underdog.sd.cli.sdcpp.response

import com.fasterxml.jackson.annotation.JsonProperty
import underdog.sd.cli.sdcpp.request.Defaults
import underdog.sd.cli.sdcpp.request.DefaultsByMode
import underdog.sd.cli.sdcpp.request.Features
import underdog.sd.cli.sdcpp.request.FeaturesByMode
import underdog.sd.cli.sdcpp.request.Limits
import underdog.sd.cli.sdcpp.request.Lora
import underdog.sd.cli.sdcpp.request.Model
import underdog.sd.cli.sdcpp.request.OutputFormatsByMode
import underdog.sd.cli.sdcpp.request.Upscaler

class CapabilitiesResponse {

    @JsonProperty("current_mode")
    String currentMode

    @JsonProperty("defaults")
    Defaults defaults

    @JsonProperty("defaults_by_mode")
    DefaultsByMode defaultsByMode

    @JsonProperty("features")
    Features features

    @JsonProperty("features_by_mode")
    FeaturesByMode featuresByMode

    @JsonProperty("limits")
    Limits limits

    @JsonProperty("loras")
    List<Lora> loras

    @JsonProperty("model")
    Model model

    @JsonProperty("output_formats")
    List<String> outputFormats

    @JsonProperty("output_formats_by_mode")
    OutputFormatsByMode outputFormatsByMode

    @JsonProperty("samplers")
    List<String> samplers

    @JsonProperty("schedulers")
    List<String> schedulers

    @JsonProperty("supported_modes")
    List<String> supportedModes

    @JsonProperty("upscalers")
    List<Upscaler> upscalers
}
