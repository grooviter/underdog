package underdog.sd.cli.sdcpp

import com.fasterxml.jackson.annotation.JsonProperty
import underdog.sd.cli.sdcpp.capabilities.Defaults
import underdog.sd.cli.sdcpp.capabilities.DefaultsByMode
import underdog.sd.cli.sdcpp.capabilities.Features
import underdog.sd.cli.sdcpp.capabilities.FeaturesByMode
import underdog.sd.cli.sdcpp.capabilities.Limits
import underdog.sd.cli.sdcpp.capabilities.Lora
import underdog.sd.cli.sdcpp.capabilities.Model
import underdog.sd.cli.sdcpp.capabilities.OutputFormatsByMode
import underdog.sd.cli.sdcpp.capabilities.Upscaler

class CapabilitiesResult {

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
