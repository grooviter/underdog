package underdog.sd.cli.sdcpp

import underdog.sd.cli.sdcpp.request.ImageGenerationRequest
import underdog.sd.cli.sdcpp.request.VideoGenerationRequest
import underdog.sd.cli.sdcpp.response.CapabilitiesResponse
import underdog.sd.cli.sdcpp.response.JobCancellationResponse
import underdog.sd.cli.sdcpp.response.JobExecutionResponse
import underdog.sd.cli.sdcpp.response.JobStatusResponse

interface SDCPPClient {

    /**
     * @return
     * @since 0.1.0
     */
    CapabilitiesResponse getCapabilities()

    /**
     * @param jobID
     * @return
     * @since 0.1.0
     */
    JobStatusResponse getJob(String jobID)

    /**
     * @param jobID
     * @return
     * @since 0.1.0
     */
    JobCancellationResponse cancelJob(String jobID)

    /**
     * @param request
     * @return
     * @since 0.1.0
     */
    JobExecutionResponse imgGen(ImageGenerationRequest request)

    /**
     * @param request
     * @return
     * @since 0.1.0
     */
    JobExecutionResponse vidGen(VideoGenerationRequest request)
}