package underdog.sd.cli.sdapi.request

import com.fasterxml.jackson.annotation.JsonProperty

class StableDiffusionCppSupportedFields {
    @JsonProperty(value = "prompt", defaultValue = "")
    String prompt = ""

    @JsonProperty(value = "negative_prompt", defaultValue = "")
    String negativePrompt = ""

    @JsonProperty(value = "width", defaultValue = "512")
    Integer width = 512

    @JsonProperty(value = "height", defaultValue = "512")
    Integer height = 512

    @JsonProperty(value = "steps")
    Integer steps

    @JsonProperty(value = "cfg_scale", defaultValue = "7.0")
    Double cfgScale = 7.0

    @JsonProperty(value = "seed", defaultValue = "-1")
    Long seed = -1

    @JsonProperty(value = "batch_size", defaultValue = "1")
    Integer batchSize = 1

    @JsonProperty("clip_skip")
    Integer clipSkip

    @JsonProperty(value = "sampler_name", defaultValue = "DPM++ 2M Karras")
    String samplerName = "DPM++ 2M Karras"

    @JsonProperty("scheduler")
    String scheduler

    @JsonProperty("lora")
    List<String> lora

    @JsonProperty("extra_images")
    List<String> extraImages

    @JsonProperty(value = "enable_hr", defaultValue = "false")
    Boolean enableHr = false

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

    @JsonProperty(value = "denoising_strength", defaultValue = "0.7")
    Double denoisingStrength = 0.7
}
