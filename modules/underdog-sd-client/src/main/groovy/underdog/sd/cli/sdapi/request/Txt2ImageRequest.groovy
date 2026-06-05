package underdog.sd.cli.sdapi.request

import com.fasterxml.jackson.annotation.JsonIgnoreProperties
import com.fasterxml.jackson.annotation.JsonInclude
import com.fasterxml.jackson.annotation.JsonProperty
import groovy.transform.builder.Builder
import underdog.sd.cli.Request

@Builder(includeSuperProperties = true)
@JsonIgnoreProperties(ignoreUnknown = true)
@JsonInclude(JsonInclude.Include.NON_NULL)
class Txt2ImageRequest extends StableDiffusionCppSupportedFields implements Request {
    @JsonProperty(value = "restore_faces", defaultValue = "false")
    Boolean restoreFaces = false

    @JsonProperty(value = "alwayson_scripts")
    Map<String, Object> alwaysonScripts = [:]

    @JsonProperty(value = "firstphase_width", defaultValue = "0")
    Integer firstphaseWidth = 0

    @JsonProperty(value = "firstphase_height", defaultValue = "0")
    Integer firstphaseHeight = 0

    @JsonProperty(value = "styles")
    List<String> styles = []

    @JsonProperty(value = "subseed", defaultValue = "-1")
    Long subseed = -1

    @JsonProperty(value = "subseed_strength", defaultValue = "0.0")
    Double subseedStrength = 0.0d

    @JsonProperty(value = "seed_resize_from_h", defaultValue = "0")
    Integer seedResizeFromH = 0

    @JsonProperty(value = "seed_resize_from_w", defaultValue = "0")
    Integer seedResizeFromW = 0

    @JsonProperty(value = "n_iter", defaultValue = "1")
    Integer nIter = 1

    @JsonProperty(value = "tiling", defaultValue = "false")
    Boolean tiling = false

    @JsonProperty(value = "do_not_save_samples", defaultValue = "false")
    Boolean doNotSaveSamples = false

    @JsonProperty(value = "do_not_save_grid", defaultValue = "false")
    Boolean doNotSaveGrid = false

    @JsonProperty(value = "eta", defaultValue = "1.0")
    Double eta = 1.0

    @JsonProperty(value = "s_churn", defaultValue = "0")
    Integer sChurn = 0

    @JsonProperty(value = "s_tmax", defaultValue = "0")
    Integer sTmax = 0

    @JsonProperty(value = "s_tmin", defaultValue = "0")
    Integer sTmin = 0

    @JsonProperty(value = "s_noise", defaultValue = "1")
    Integer sNoise = 1

    @JsonProperty(value = "override_settings")
    Map<String, Object> overrideSettings = [:]

    @JsonProperty(value = "override_settings_restore_afterwards", defaultValue = "true")
    Boolean overrideSettingsRestoreAfterwards = true

    @JsonProperty(value = "script_args")
    List<String> scriptArgs

    @JsonProperty(value = "script_name")
    String scriptName

    @JsonProperty(value = "send_images", defaultValue = "true")
    Boolean sendImages = true

    @JsonProperty(value = "save_images", defaultValue = "false")
    Boolean saveImages = false

    @JsonProperty(value = "sampler_index")
    @Deprecated
    String samplerIndex

    @JsonProperty(value = "use_deprecated_controlnet", defaultValue = "false")
    Boolean useDeprecatedControlnet = false
}