package underdog.sd.cli.http;

import com.fasterxml.jackson.annotation.JsonProperty
import groovy.transform.ToString

@ToString
class BadRequestException extends ServerException {
  @JsonProperty("error")
  private String error

  @JsonProperty("detail")
  private String detail

  @JsonProperty("body")
  private String body

  @JsonProperty("errors")
  private String errors
}
