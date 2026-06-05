package underdog.sd.cli.sdapi.response

import com.fasterxml.jackson.annotation.JsonProperty

class SDAPIOptionsResponse {
    @JsonProperty("samples_format")
    String samplesFormat

    @JsonProperty("sd_model_checkpoint")
    String sdModelCheckpoint
}
