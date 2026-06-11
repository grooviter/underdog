package underdog.guide.sd

import spock.lang.Shared
import spock.lang.Specification
import underdog.sd.cli.ApiOptions
import underdog.sd.cli.SD
import underdog.sd.cli.sdcpp.SDCPPClient
import underdog.sd.cli.sdcpp.request.ImageGenerationRequest
import underdog.sd.cli.sdcpp.response.JobExecutionResponse
import underdog.sd.cli.sdcpp.response.JobStatusResponse

class SDCppSpec extends Specification {

    @Shared
    SDCPPClient client = SD.sdcpp(ApiOptions.builder()
        .baseUrl('https://sdcpp.lab.bit2lab.com')
        .build())

    def "restoring" () {
        when:
        String prompt = """\
        | Please restore and enhance the quality of this photo. Fix tears, scratches, discoloration, 
        | sharpen the details, and colorize it
        """.asPrompt()

        File images = new File('src/test/resources/underdog/sd/sdapi')

        JobExecutionResponse response = client.imgGen(ImageGenerationRequest.builder()
            .prompt(prompt)
            .refImages([new File(images, 'eiffel_tower.jpg').fileToBase64()])
            .width(1900)
            .height(1072)
            .seed(200_000_002)
            .cfgScale(4.5)
            .strength(0.28)
        .build())

        JobStatusResponse withImageResponse = awaitUntilJobStatusResult(response.id)
        File imageFile = withImageResponse.result.images[0].b64JSON.base64ToTempFile()

        then:
        imageFile.exists()
    }

    private JobStatusResponse awaitUntilJobStatusResult(String jobID) {
        JobStatusResponse statusResult = client.getJob((jobID))
        while (!statusResult.completed) {
            statusResult = client.getJob(jobID)
            Thread.sleep(1_000)
        }
        return statusResult
    }
}
