package underdog.sd.cli.sdapi

import groovy.transform.TupleConstructor
import underdog.sd.cli.http.HTTPService
import underdog.sd.cli.sdapi.request.Image2ImageRequest
import underdog.sd.cli.sdapi.request.Txt2ImageRequest
import underdog.sd.cli.sdapi.response.Image2ImageResponse
import underdog.sd.cli.sdapi.response.LatentUpscaleModesResponse
import underdog.sd.cli.sdapi.response.LoraResponse
import underdog.sd.cli.sdapi.response.SDAPIOptionsResponse
import underdog.sd.cli.sdapi.response.SDModelResponse
import underdog.sd.cli.sdapi.response.SamplersResponse
import underdog.sd.cli.sdapi.response.SchedulersResponse
import underdog.sd.cli.sdapi.response.Txt2ImgResponse
import underdog.sd.cli.sdapi.response.UpscalersResponse

@TupleConstructor
class SDAPIClientImplementation implements SDAPIClient {
    HTTPService httpService

    @Override
    List<SDModelResponse> getAvailableModels() {
        return httpService.executeGET('/sdapi/v1/sd-models', SDModelResponse[])
    }

    @Override
    List<LoraResponse> getLoras() {
        return httpService.executeGET('/sdapi/v1/loras', LoraResponse[])
    }

    @Override
    List<UpscalersResponse> getUpscalers() {
        return httpService.executeGET('/sdapi/v1/upscalers', UpscalersResponse[])
    }

    @Override
    List<LatentUpscaleModesResponse> getLatentUpscaleModes() {
        return httpService.executeGET('/sdapi/v1/latent-upscale-modes', LatentUpscaleModesResponse[])
    }

    @Override
    List<SamplersResponse> getSamplers() {
        return httpService.executeGET('/sdapi/v1/samplers', SamplersResponse[])
    }

    @Override
    List<SchedulersResponse> getSchedulers() {
        return httpService.executeGET('/sdapi/v1/schedulers', SchedulersResponse[])
    }

    @Override
    SDAPIOptionsResponse getOptions() {
        return httpService.executeGET('/sdapi/v1/options', SDAPIOptionsResponse)
    }

    @Override
    Image2ImageResponse img2img(Image2ImageRequest request) {
        return httpService.executePOST('/sdapi/v1/img2img', request, Image2ImageResponse)
    }

    @Override
    Txt2ImgResponse txt2Img(Txt2ImageRequest request) {
        return httpService.executePOST('/sdapi/v1/txt2img', request, Txt2ImgResponse)
    }
}
