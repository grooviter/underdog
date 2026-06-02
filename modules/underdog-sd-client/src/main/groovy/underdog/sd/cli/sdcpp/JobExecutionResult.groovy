package underdog.sd.cli.sdcpp

import com.fasterxml.jackson.annotation.JsonProperty

class JobExecutionResult {
    @JsonProperty("created")
    Long created

    @JsonProperty("id")
    String id

    @JsonProperty("kind")
    String kind

    @JsonProperty("poll_url")
    String pollURL

    @JsonProperty("status")
    String status
}
