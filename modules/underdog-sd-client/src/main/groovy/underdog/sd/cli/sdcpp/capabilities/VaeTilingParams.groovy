package underdog.sd.cli.sdcpp.capabilities

import com.fasterxml.jackson.annotation.JsonProperty

class VaeTilingParams {
    @JsonProperty("enabled")
    Boolean enabled

    @JsonProperty("rel_size_x")
    Double relSizeX

    @JsonProperty("rel_size_y")
    Double relSizeY

    @JsonProperty("target_overlap")
    Double targetOverlap

    @JsonProperty("tile_size_x")
    Double tileSizeX

    @JsonProperty("tile_size_y")
    Double tileSizeY
}
