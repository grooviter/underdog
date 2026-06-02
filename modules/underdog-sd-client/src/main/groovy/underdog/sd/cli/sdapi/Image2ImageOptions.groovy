package underdog.sd.cli.sdapi

import com.fasterxml.jackson.annotation.JsonIgnoreProperties
import com.fasterxml.jackson.annotation.JsonInclude
import com.fasterxml.jackson.annotation.JsonProperty
import groovy.transform.builder.Builder
import underdog.sd.cli.Options

@Builder
@JsonIgnoreProperties(ignoreUnknown = true)
@JsonInclude(JsonInclude.Include.NON_NULL)
class Image2ImageOptions implements Options {

  @JsonProperty(value  = "prompt", defaultValue = "")
  String prompt = ""

  @JsonProperty(value  = "negative_prompt", defaultValue = "")
  String negativePrompt = ""

  @JsonProperty(value  = "styles")
  List<String> styles = []

  @JsonProperty(value  = "seed", defaultValue = "-1")
  Integer seed = -1

  @JsonProperty(value  = "subseed", defaultValue = "-1")
  Integer subseed = -1

  @JsonProperty(value  = "subseed_strength", defaultValue = "0")
  Integer subseedStrength = 0

  @JsonProperty(value  = "seed_resize_from_h", defaultValue = "-1")
  Integer seedResizeFromH = -1

  @JsonProperty(value  = "seed_resize_from_w", defaultValue = "-1")
  Integer seedResizeFromW = -1

  @JsonProperty(value  = "sampler_name", defaultValue = "euler")
  String samplerName = "euler"

  @JsonProperty(value  = "batch_size", defaultValue = "1")
  Integer batchSize = 1

  @JsonProperty(value  = "n_iter", defaultValue = "1")
  Integer nIter = 1

  @JsonProperty(value  = "steps", defaultValue = "50")
  Integer steps = 50

  @JsonProperty(value  = "cfg_scale", defaultValue = "7")
  Double cfgScale = 7

  @JsonProperty(value  = "width", defaultValue = "512")
  Integer width = 512

  @JsonProperty(value  = "height", defaultValue = "512")
  Integer height = 512

  @JsonProperty(value  = "restore_faces", defaultValue = "true")
  Boolean restoreFaces = true

  @JsonProperty(value  = "tiling", defaultValue = "true")
  Boolean tiling = true

  @JsonProperty(value  = "do_not_save_samples", defaultValue = "false")
  Boolean doNotSaveSamples = false

  @JsonProperty(value  = "do_not_save_grid", defaultValue = "false")
  Boolean doNotSaveGrid = false

  @JsonProperty(value  = "eta", defaultValue = "0")
  Integer eta = 0

  @JsonProperty(value  = "denoising_strength", defaultValue = "0.75")
  Double denoisingStrength = 0.75

  @JsonProperty(value  = "s_min_uncond", defaultValue = "0")
  Integer sMinUncond = 0

  @JsonProperty(value  = "s_churn", defaultValue = "0")
  Integer sChurn = 0

  @JsonProperty(value  = "s_tmax", defaultValue = "0")
  Integer sTmax = 0

  @JsonProperty(value  = "s_tmin", defaultValue = "0")
  Integer sTmin = 0

  @JsonProperty(value  = "s_noise", defaultValue = "0")
  Integer sNoise = 0

  @JsonProperty(value  = "override_settings")
  Map<String, Object> overrideSettings = [:]

  @JsonProperty(value  = "override_settings_restore_afterwards", defaultValue = "false")
  Boolean overrideSettingsRestoreAfterwards = false

  @JsonProperty(value  = "refiner_checkpoint", defaultValue = "")
  String refinerCheckpoint = ""

  @JsonProperty(value  = "refiner_switch_at", defaultValue = "0")
  Integer refinerSwitchAt = 0

  @JsonProperty(value  = "disable_extra_networks", defaultValue = "false")
  Boolean disableExtraNetworks = false

  @JsonProperty(value  = "comments")
  Map<String, Object> comments = [:]

  @JsonProperty(value  = "init_images", defaultValue = "[]")
  List<String> initImages = []

  @JsonProperty(value  = "resize_mode", defaultValue = "0")
  Integer resizeMode = 0

  @JsonProperty(value  = "image_cfg_scale", defaultValue = "0")
  Integer imageCfgScale = 0

  @JsonProperty(value  = "mask")
  String mask

  @JsonProperty(value  = "mask_blur_x", defaultValue = "0")
  Integer maskBlurX = 0

  @JsonProperty(value  = "mask_blur_y", defaultValue = "0")
  Integer maskBlurY = 0

  @JsonProperty(value  = "mask_blur", defaultValue = "0")
  Integer maskBlur = 0

  @JsonProperty(value  = "inpainting_fill", defaultValue = "0")
  Integer inpaintingFill = 0

  @JsonProperty(value  = "inpaint_full_res", defaultValue = "false")
  Boolean inpaintFullRes = false

  @JsonProperty(value  = "inpaint_full_res_padding", defaultValue = "0")
  Integer inpaintingFullResPadding = 0

  @JsonProperty(value  = "inpainting_mask_invert", defaultValue = "0")
  Integer inpaintingMaskInvert = 0

  @JsonProperty(value  = "initial_noise_multiplier", defaultValue = "0")
  Integer initialNoiseMultiplier = 0

  @JsonProperty(value  = "latent_mask", defaultValue = "")
  String latentMask = ""

  @JsonProperty(value  = "sampler_index", defaultValue = "")
  @Deprecated
  String samplerIndex = ""

  @JsonProperty(value  = "include_init_images", defaultValue = "false")
  Boolean includeInitImages = false

  @JsonProperty(value  = "script_name", defaultValue = "")
  String scriptName = ""

  @JsonProperty(value  = "script_args")
  String[] scriptArgs

  @JsonProperty(value  = "send_images", defaultValue = "true")
  Boolean sendImages = true

  @JsonProperty(value  = "save_images", defaultValue = "false")
  Boolean saveImages = false

  @JsonProperty(value  = "alwayson_scripts")
  Map<String, Object> alwaysonScripts = [:]
}
