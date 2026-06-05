package underdog.sd.cli.sdcpp

import groovy.transform.TupleConstructor
import underdog.sd.cli.http.HTTPService
import underdog.sd.cli.sdcpp.request.ImageGenerationRequest
import underdog.sd.cli.sdcpp.request.VideoGenerationRequest
import underdog.sd.cli.sdcpp.response.CapabilitiesResponse
import underdog.sd.cli.sdcpp.response.JobCancellationResponse
import underdog.sd.cli.sdcpp.response.JobExecutionResponse
import underdog.sd.cli.sdcpp.response.JobStatusResponse

@TupleConstructor
class SDCPPClientImplementation implements SDCPPClient {
    HTTPService httpService

    @Override
    CapabilitiesResponse getCapabilities() {
        return httpService.executeGET('/sdcpp/v1/capabilities', CapabilitiesResponse)
    }

    @Override
    JobStatusResponse getJob(String jobID) {
        return httpService.executeGET("/sdcpp/v1/jobs/$jobID", JobStatusResponse)
    }

    @Override
    JobCancellationResponse cancelJob(String jobID) {
        return httpService.executePOST("/sdcpp/v1/jobs/$jobID/cancel",null, JobCancellationResponse)
    }

    @Override
    JobExecutionResponse imgGen(ImageGenerationRequest request) {
        return httpService.executePOST('/sdcpp/v1/img_gen', request, JobExecutionResponse)
    }

    @Override
    JobExecutionResponse vidGen(VideoGenerationRequest request) {
        return httpService.executePOST('/sdcpp/v1/vid_gen', request, JobExecutionResponse)
    }
}
