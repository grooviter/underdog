package underdog.sd.cli.sdcpp

import com.fasterxml.jackson.annotation.JsonProperty

class JobCancellationResult {
    @JsonProperty("error")
    String error
}
