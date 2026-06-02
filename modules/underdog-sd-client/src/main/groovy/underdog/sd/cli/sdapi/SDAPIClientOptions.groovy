package underdog.sd.cli.sdapi

import groovy.transform.builder.Builder
import underdog.sd.cli.Options

@Builder
class SDAPIClientOptions implements Options {
  String endpoint
}
