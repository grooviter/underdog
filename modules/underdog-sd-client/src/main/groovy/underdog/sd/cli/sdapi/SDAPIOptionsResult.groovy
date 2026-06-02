package underdog.sd.cli.sdapi

import com.fasterxml.jackson.annotation.JsonProperty

class SDAPIOptionsResult {
    @JsonProperty("samples_format")
    String samplesFormat

    @JsonProperty("sd_model_checkpoint")
    String sdModelCheckpoint
}
