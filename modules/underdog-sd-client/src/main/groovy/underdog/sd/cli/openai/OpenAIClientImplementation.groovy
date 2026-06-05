package underdog.sd.cli.openai

import groovy.transform.TupleConstructor
import org.apache.hc.client5.http.entity.mime.MultipartEntityBuilder
import org.apache.hc.core5.http.ContentType
import underdog.sd.cli.Images
import underdog.sd.cli.http.HTTPService

@TupleConstructor
class OpenAIClientImplementation implements OpenAIClient {
    HTTPService httpService

    @Override
    ImagesResponse imageGeneration(GenerationsRequest request) {
        return httpService.executePOST('/v1/images/generations', request, ImagesResponse)
    }

    @Override
    ModelsResponse models() {
        return httpService.executeGET('/v1/models', ModelsResponse)
    }

    @Override
    ImagesResponse edits(EditsRequest request) {
        MultipartEntityBuilder builder = MultipartEntityBuilder.create()
            .addTextBody("model", request.model)
            .addTextBody("prompt", request.prompt)

        if (request.n) {
            builder.addTextBody("n", request.n.toString())
        }

        if (request.quality) {
            builder.addTextBody("input_fidelity", request.quality.toString())
        }

        if (request.background) {
            builder.addTextBody("background", request.background.toString())
        }

        if (request.moderation){
            builder.addTextBody("moderation", request.moderation.toString())
        }

        if (request.outputFormat) {
            builder.addTextBody("output_format", request.outputFormat.toString())
        }

        if (request.partialImages) {
            builder.addTextBody("partial_images", request.partialImages.toString())
        }

        if (request.size) {
            builder.addTextBody("size", request.size)
        }

        request.images?.each { image ->
            File tempFile = Images.base64ToTempFile(image.imageURL)
            builder.addBinaryBody("image", tempFile, ContentType.IMAGE_PNG, tempFile.name)
        }

        if (request.mask) {
            File tempFile = Images.base64ToTempFile(request.mask.imageURL)
            builder.addBinaryBody("mask", tempFile, ContentType.IMAGE_PNG, tempFile.name)
        }

        return httpService.executeMultipartDataPOST('/v1/images/edits', builder.build(), ImagesResponse)
    }
}
