package underdog.sd.cli.openai

import underdog.sd.cli.openai.request.EditsRequest
import underdog.sd.cli.openai.request.GenerationsRequest
import underdog.sd.cli.openai.response.ImagesResponse
import underdog.sd.cli.openai.response.ModelsResponse

interface OpenAIClient {

    ImagesResponse imageGeneration(GenerationsRequest request)

    ModelsResponse models()

    ImagesResponse edits(EditsRequest request)
}