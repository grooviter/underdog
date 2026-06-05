package underdog.sd.cli.sdcpp.domain

import com.fasterxml.jackson.annotation.JsonProperty

class Guidance {
    @JsonProperty("distilled_guidance")
    Double distilledGuidance

    // TODO img_cfg not idea how it could look like (map, string...)
    @JsonProperty("img_cfg")
    String imgCfg

    @JsonProperty("slg")
    Slug slug

    @JsonProperty("txt_cfg")
    Double txtCfg
}
