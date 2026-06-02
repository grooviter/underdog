package underdog.sd.cli

import spock.lang.Ignore
import underdog.sd.cli.common.SDAwareSpec
import underdog.sd.cli.sdcpp.CapabilitiesResult
import underdog.sd.cli.sdcpp.ImageGenerationOptions
import underdog.sd.cli.sdcpp.JobExecutionResult
import underdog.sd.cli.sdcpp.JobStatusResult

class SDCPPClientSpec extends SDAwareSpec{
    def "/sdcpp/capabilities"() {
        when:
        CapabilitiesResult result = sdcpp.capabilities

        then:
        result

        and:
        result.model.name
        result.model.path
        result.model.stem

        and:
        result.schedulers.size() > 0
        result.samplers.size() > 0
    }

    def '/sdcpp/job/$id'() {
        when:
        JobExecutionResult executionResult = sdcpp.imgGen(ImageGenerationOptions.builder()
            .prompt("A dog")
            .cfgScale(1)
            .denoisingStrength(0.7)
            .samplerName("euler")
            .seed(200_002)
            .width(256)
            .height(256)
            .steps(4)
            .build())

        and:
        JobStatusResult statusResult = sdcpp.getJob(executionResult.id)

        then:
        statusResult.created
        statusResult.started
        statusResult.id == executionResult.id
        statusResult.status == 'generating'
    }

    @Ignore
    def '/sdcpp/job/$id/cancel'() {
        expect:
        false
    }

    def '/sdcpp/v1/img_gen'() {
        when:
        JobExecutionResult executionResult = sdcpp.imgGen(ImageGenerationOptions.builder()
                .prompt("A dog")
                .cfgScale(1)
                .denoisingStrength(0.7)
                .samplerName("euler")
                .seed(200_002)
                .width(256)
                .height(256)
                .steps(4)
                .build())

        and:
        JobStatusResult statusResult = sdcpp.getJob(executionResult.id)
        while (!statusResult.completed) {
            statusResult = sdcpp.getJob(executionResult.id)
            Thread.sleep(1_000)
        }

        then:
        statusResult.id == executionResult.id
        statusResult.result.images.size() > 0
        statusResult.completed
    }

    @Ignore
    def '/sdcpp/v1/vid_gen'() {
        expect:
        false
    }
}
