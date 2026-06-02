package underdog.sd.cli.sdcpp

import com.fasterxml.jackson.annotation.JsonProperty


class JobStatusResult {

    static class Image {
        @JsonProperty("b64_json")
        String b64JSON

        @JsonProperty("index")
        Integer index
    }

    static class Result {
        @JsonProperty("output_format")
        String outputFormat

        @JsonProperty("images")
        List<Image> images
    }

    @JsonProperty("completed")
    Long completed

    @JsonProperty("created")
    Long created

    @JsonProperty("error")
    String error

    @JsonProperty("id")
    String id

    @JsonProperty("kind")
    String kind

    @JsonProperty("queue_position")
    Long queue_position

    @JsonProperty("result")
    Result result

    @JsonProperty("started")
    Long started

    @JsonProperty("status")
    String status
}
