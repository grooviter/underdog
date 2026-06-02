package underdog.sd.cli.sdcpp

import groovy.transform.TupleConstructor
import underdog.sd.cli.http.HTTPService

@TupleConstructor
class SDCPPClientImplementation implements SDCPPClient {
    HTTPService httpService

    @Override
    CapabilitiesResult getCapabilities() {
        return httpService.executeGET('/sdcpp/v1/capabilities', CapabilitiesResult)
    }

    @Override
    JobStatusResult getJob(String jobID) {
        return httpService.executeGET("/sdcpp/v1/jobs/$jobID", JobStatusResult)
    }

    @Override
    JobCancellationResult cancelJob(String jobID) {
        return httpService.executePOST("/sdcpp/v1/jobs/$jobID/cancel",null, JobCancellationResult)
    }

    @Override
    JobExecutionResult imgGen(ImageGenerationOptions options) {
        return httpService.executePOST('/sdcpp/v1/img_gen', options, JobExecutionResult)
    }

    @Override
    JobExecutionResult vidGen(VideoGenerationOptions options) {
        return httpService.executePOST('/sdcpp/v1/vid_gen', options, JobExecutionResult)
    }
}
