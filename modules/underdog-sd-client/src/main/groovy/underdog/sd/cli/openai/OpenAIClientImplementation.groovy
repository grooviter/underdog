package underdog.sd.cli.openai

import groovy.transform.TupleConstructor
import org.apache.hc.client5.http.entity.mime.MultipartEntityBuilder
import org.apache.hc.core5.http.ContentType
import org.apache.hc.core5.http.HttpEntity
import underdog.sd.cli.Images
import underdog.sd.cli.http.HTTPService

@TupleConstructor
class OpenAIClientImplementation implements OpenAIClient {
    HTTPService httpService

    @Override
    ImagesResult imageGeneration(GenerationsOptions options) {
        return httpService.executePOST('/v1/images/generations', options, ImagesResult)
    }

    @Override
    ModelsResult models() {
        return httpService.executeGET('/v1/models', ModelsResult)
    }

    @Override
    ImagesResult edits(EditsOptions options) {
        MultipartEntityBuilder builder = MultipartEntityBuilder.create()
            .addTextBody("model", options.model)
            .addTextBody("prompt", options.prompt)

        if (options.n) {
            builder.addTextBody("n", options.n.toString())
        }

        if (options.quality) {
            builder.addTextBody("input_fidelity", options.quality.toString())
        }

        if (options.background) {
            builder.addTextBody("background", options.background.toString())
        }

        if (options.moderation){
            builder.addTextBody("moderation", options.moderation.toString())
        }

        if (options.outputFormat) {
            builder.addTextBody("output_format", options.outputFormat.toString())
        }

        if (options.partialImages) {
            builder.addTextBody("partial_images", options.partialImages.toString())
        }

        if (options.size) {
            builder.addTextBody("size", options.size)
        }

        options.images?.each {image ->
            File tempFile = Images.base64ToTempFile(image.imageURL)
            builder.addBinaryBody("image", tempFile, ContentType.IMAGE_PNG, tempFile.name)
        }

        if (options.mask) {
            File tempFile = Images.base64ToTempFile(options.mask.imageURL)
            builder.addBinaryBody("mask", tempFile, ContentType.IMAGE_PNG, tempFile.name)
        }

        return httpService.executeMultipartDataPOST('/v1/images/edits', builder.build(), ImagesResult)
    }
}
