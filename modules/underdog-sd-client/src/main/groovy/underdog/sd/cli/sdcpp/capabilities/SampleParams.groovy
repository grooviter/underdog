package underdog.sd.cli.sdcpp.capabilities

import com.fasterxml.jackson.annotation.JsonProperty

class SampleParams {
    @JsonProperty("eta")
    Long eta

    @JsonProperty("flow_shift")
    Double flowShift

    @JsonProperty("guidance")
    Guidance guidance

    @JsonProperty("sample_method")
    String sampleMethod

    @JsonProperty("sample_steps")
    Integer sampleSteps

    @JsonProperty("scheduler")
    String scheduler

    @JsonProperty("shifted_timestep")
    Integer shiftedTimestep
}
