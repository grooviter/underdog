package underdog.sd.cli

import com.fasterxml.jackson.annotation.JsonProperty

/**
 * @since 0.1.0
 */
class Txt2ImageBaseOptions implements Options {
    @JsonProperty(value = "enable_hr", defaultValue = "false")
    Boolean enableHr = false

    @JsonProperty(value = "denoising_strength", defaultValue = "0.7")
    Double denoisingStrength = 0.7

    @JsonProperty(value = "firstphase_width", defaultValue = "0")
    Integer firstphaseWidth = 0

    @JsonProperty(value = "firstphase_height", defaultValue = "0")
    Integer firstphaseHeight = 0

    @JsonProperty(value = "hr_scale", defaultValue = "2")
    Integer hrScale = 2

    @JsonProperty(value = "hr_upscaler", defaultValue = 'Latent')
    HiResUpscaler hrUpscaler = "Latent"

    @JsonProperty(value = "hr_second_pass_steps", defaultValue = "0")
    Integer hrSecondPassSteps = 0

    @JsonProperty(value = "hr_resize_x", defaultValue = "0")
    Integer hrResizeX = 0

    @JsonProperty(value = "hr_resize_y", defaultValue = "0")
    Integer hrResizeY = 0

    @JsonProperty(value = "prompt", defaultValue = "")
    String prompt = ""

    @JsonProperty(value = "styles")
    List<String> styles = []

    @JsonProperty(value = "seed", defaultValue = "-1")
    Long seed = -1

    @JsonProperty(value = "subseed", defaultValue = "-1")
    Long subseed = -1

    @JsonProperty(value = "subseed_strength", defaultValue = "0.0")
    Double subseedStrength = 0.0d

    @JsonProperty(value = "seed_resize_from_h", defaultValue = "0")
    Integer seedResizeFromH = 0

    @JsonProperty(value = "seed_resize_from_w", defaultValue = "0")
    Integer seedResizeFromW = 0

    @JsonProperty(value = "sampler_name", defaultValue = "DPM++ 2M Karras")
    String samplerName = "DPM++ 2M Karras"

    @JsonProperty(value = "batch_size", defaultValue = "1")
    Integer batchSize = 1

    @JsonProperty(value = "n_iter", defaultValue = "1")
    Integer nIter = 1

    @JsonProperty(value = "steps")
    Integer steps

    @JsonProperty(value = "cfg_scale", defaultValue = "7.0")
    Double cfgScale = 7.0

    @JsonProperty(value = "width", defaultValue = "512")
    Integer width = 512

    @JsonProperty(value = "height", defaultValue = "512")
    Integer height = 512

    @JsonProperty(value = "tiling", defaultValue = "false")
    Boolean tiling = false

    @JsonProperty(value = "do_not_save_samples", defaultValue = "false")
    Boolean doNotSaveSamples = false

    @JsonProperty(value = "do_not_save_grid", defaultValue = "false")
    Boolean doNotSaveGrid = false

    @JsonProperty(value = "negative_prompt", defaultValue = "")
    String negativePrompt = ""

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
