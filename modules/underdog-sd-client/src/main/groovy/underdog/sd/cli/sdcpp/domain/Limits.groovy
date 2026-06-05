package underdog.sd.cli.sdcpp.domain

import com.fasterxml.jackson.annotation.JsonProperty

class Limits {
    @JsonProperty("max_batch_count")
    Integer maxBatchCount

    @JsonProperty("max_height")
    Integer maxHeight

    @JsonProperty("max_queue_size")
    Integer maxQueueSize

    @JsonProperty("max_width")
    Integer maxWidth

    @JsonProperty("min_height")
    Integer minHeight

    @JsonProperty("min_width")
    Integer minWidth
}
