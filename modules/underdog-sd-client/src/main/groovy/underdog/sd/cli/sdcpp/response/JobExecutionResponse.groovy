package underdog.sd.cli.sdcpp.response

import com.fasterxml.jackson.annotation.JsonProperty

class JobExecutionResponse {
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
