package underdog.sd.cli

import spock.lang.TempDir
import underdog.sd.cli.common.SDAwareSpec
import underdog.sd.cli.sdapi.Image2ImageOptions
import underdog.sd.cli.sdapi.Image2ImageResult
import underdog.sd.cli.sdapi.LatentUpscaleModesResult
import underdog.sd.cli.sdapi.LoraResult
import underdog.sd.cli.sdapi.SDAPIOptionsResult
import underdog.sd.cli.sdapi.SDModelResult
import underdog.sd.cli.sdapi.SamplersResult
import underdog.sd.cli.sdapi.SchedulersResult
import underdog.sd.cli.sdapi.Txt2ImageOptions
import underdog.sd.cli.sdapi.Txt2ImgResult
import underdog.sd.cli.sdapi.UpscalersResult

import java.nio.file.Path

class SDAPIClientSpec extends SDAwareSpec {
    def "/sdapi/v1/sd-models"() {
        when:
        List<SDModelResult> availableModels = sdapi.availableModels

        then:
        availableModels.size() == 1
    }

    def "/sdapi/v1/loras"() {
        when:
        List<LoraResult> loras = sdapi.loras

        then:
        loras.size() == 1
    }

    def "/sdapi/v1/upscalers"() {
        when:
        List<UpscalersResult> upscalers = sdapi.upscalers

        then:
        upscalers.size() == 3
    }

    def "/sdapi/v1/latent-upscale-modes"() {
        when:
        List<LatentUpscaleModesResult> upscaleModesResults = sdapi.latentUpscaleModes

        then:
        upscaleModesResults.size() == 6
    }

    def "/sdapi/v1/samplers"() {
        when:
        List<SamplersResult> samplers = sdapi.samplers

        then:
        samplers.size() == 19
    }

    def "/sdapi/v1/schedulers"() {
        when:
        List<SchedulersResult> schedulers = sdapi.schedulers

        then:
        schedulers.size() == 13
    }

    def "/sdapi/v1/options"() {
        when:
        SDAPIOptionsResult optionsResult = sdapi.options

        then:
        optionsResult.sdModelCheckpoint

        and:
        optionsResult.samplesFormat
    }

    def "/sdapi/v1/txt2img"() {
        when:
        Txt2ImageOptions options = Txt2ImageOptions.builder()
            .prompt("A dog")
            .height(393)
            .width(333)
            .cfgScale(4)
            .steps(1)
            .denoisingStrength(1)
            .build()

        and:
        Txt2ImgResult result = sdapi.txt2Img(options)

        then:
        result.images.size() == 1
    }

    def "/sdapi/v1/img2img (init_images)"() {
        setup:
        String prompt = """\
        | highly realistic restored color photograph of a man with a 
        | tribal tattoo in half of his face, natural skin tones, historically 
        | accurate colors, detailed, subtle film grain
        """

        String negativePrompt = """\
        | black and white, grayscale, oversaturated, cartoon, distorted, 
        | blurry, unrealistic colors
        """

        when:
        Image2ImageOptions options = Image2ImageOptions.builder()
            .initImages([alfredHitchcockBase64Image])
            .prompt(prompt.stripMargin().stripIndent())
            .negativePrompt(negativePrompt.stripMargin().stripIndent())
            .height(512)
            .width(512)
            .seed(200_000)
            .cfgScale(4.5)
            .samplerName("DPM++ 2M Karras")
            .denoisingStrength(0.28)
            .build()

        and:
        Image2ImageResult result = sdapi.img2img(options)
        File tempFile = Images.base64ToTempFile(result.images[0])

        then:
        tempFile
        result.images.size() == 1
    }

    def '/sdapi/v1/img2img (init_images + mask)'(@TempDir Path outputPath) {
        setup:
        String prompt = """\
        | A roundabout surrounded by a continuos urban european park. The filled-in area should match 
        | the perspective, lighting, and camera angle of the original photograph. Create a realistic park 
        | environment with grass, small trees, shrubs, and paved pedestrian paths that seamlessly connect 
        | with the existing surroundings of the roundabout. There must be no buildings in the roundabout.
        | Maintain coherent shadows and lighting direction so the result looks physically plausible and unedited. 
        | Ensure there are no traces, outlines, or artifacts of the removed monument. 
        | The final image should appear as an uninterrupted, naturally designed circular park integrated 
        | into the city environment.
        """.stripMargin().stripIndent()

        String negativePrompt = """\
        | monument, statue, sculpture, memorial, obelisk, column, fountain centerpiece, architectural centerpiece, 
        | landmark structure, ruins, pedestal, pedestal base, structure remnants, carved stone, historical figure, 
        | text, inscriptions, plaques, signage, people, vehicles overlapping the center, distorted perspective, 
        | warped geometry, blurry textures, low detail, smudges, ghosting, double objects, duplicate structures, 
        | artificial symmetry, floating objects, unrealistic shadows, mismatched lighting, inconsistent scale, 
        | artifacts, noise, overexposed, underexposed
        """.stripMargin().stripIndent()

        when:
        Image2ImageOptions options = Image2ImageOptions.builder()
            .initImages([puertaDeAlcalaBase64Image])
            .mask(puertaDeAlcalaMaskBase64Image)
            .seed(2000)
            .cfgScale(cfgScale)
            .denoisingStrength(denoising)
            .steps(steps)
            .width(1024)
            .height(768)
            .samplerName(samplers)
            .prompt(prompt)
            .negativePrompt(negativePrompt)
            .build()


        and:
        Image2ImageResult result = sdapi.img2img(options)
        File destination = new File(outputPath.toFile(),"ds-${denoising}-cfg-${cfgScale}-sampler-${samplers}-steps-${steps}.png")
        Images.base64ToFile(result.images[0], destination)

        then:
        result.images

        where:
        denoising | cfgScale | samplers    | steps
        0.9       |    1.0   |  "euler_a"  | 20
        1.0       |    1.0   |  "euler_a"  | 20
        0.9       |    1.0   |  "lcm"      | 8
        1.0       |    1.0   |  "lcm"      | 8
    }

    def '/sdapi/v1/txt2img (rescaling)'() {
        setup:
        String prompt = """\
        | highly realistic photograph of a man
        """.stripMargin().stripIndent()

        String negativePrompt = """\
        | black and white, grayscale, oversaturated, cartoon, distorted, 
        | blurry, unrealistic colors
        """.stripMargin().stripIndent()

        and:
        def (srcWidth, srcHeight) = Images.getDimensions(
                Images.base64ToTempFile(puertaDeAlcalaBase64Image))

        when:
        Image2ImageOptions options = Image2ImageOptions.builder()
            .initImages([puertaDeAlcalaBase64Image])
            .prompt(prompt)
            .negativePrompt(negativePrompt)
            .denoisingStrength(0.1)
            .cfgScale(7)
            .width(1024)
            .height(768)
            .samplerName("euler_a")
            .steps(20)
            .build()

        and:
        def (dstWidth, dstHeight) = Images.getDimensions(
            Images.base64ToTempFile(sdapi.img2img(options).images[0]))

        then:
        dstWidth == 1024
        dstHeight == 768

        and:
        srcWidth > dstWidth
        srcHeight > dstHeight
    }
}
