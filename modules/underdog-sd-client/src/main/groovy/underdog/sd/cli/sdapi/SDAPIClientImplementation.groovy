package underdog.sd.cli.sdapi

import groovy.transform.TupleConstructor
import underdog.sd.cli.http.HTTPService

@TupleConstructor
class SDAPIClientImplementation implements SDAPIClient {
    HTTPService httpService

    @Override
    List<SDModelResult> getAvailableModels() {
        return httpService.executeGET('/sdapi/v1/sd-models', SDModelResult[])
    }

    @Override
    List<LoraResult> getLoras() {
        return httpService.executeGET('/sdapi/v1/loras', LoraResult[])
    }

    @Override
    List<UpscalersResult> getUpscalers() {
        return httpService.executeGET('/sdapi/v1/upscalers', UpscalersResult[])
    }

    @Override
    List<LatentUpscaleModesResult> getLatentUpscaleModes() {
        return httpService.executeGET('/sdapi/v1/latent-upscale-modes', LatentUpscaleModesResult[])
    }

    @Override
    List<SamplersResult> getSamplers() {
        return httpService.executeGET('/sdapi/v1/samplers', SamplersResult[])
    }

    @Override
    List<SchedulersResult> getSchedulers() {
        return httpService.executeGET('/sdapi/v1/schedulers', SchedulersResult[])
    }

    @Override
    SDAPIOptionsResult getOptions() {
        return httpService.executeGET('/sdapi/v1/options', SDAPIOptionsResult)
    }

    @Override
    Image2ImageResult img2img(Image2ImageOptions options) {
        return httpService.executePOST('/sdapi/v1/img2img', options, Image2ImageResult)
    }

    @Override
    Txt2ImgResult txt2Img(Txt2ImageOptions options) {
        return httpService.executePOST('/sdapi/v1/txt2img', options, Txt2ImgResult)
    }
}
