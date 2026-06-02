package underdog.sd.cli.sdapi;

import com.fasterxml.jackson.annotation.JsonProperty

class Image2ImageResult {

  @JsonProperty("images")
  List<String> images

  @JsonProperty("parameters")
  Image2ImageOptions parameters

  @JsonProperty("info")
  String info
}
