package underdog.sd.cli.sdcpp.response

import com.fasterxml.jackson.annotation.JsonProperty

class JobCancellationResponse {
    @JsonProperty("error")
    String error
}
