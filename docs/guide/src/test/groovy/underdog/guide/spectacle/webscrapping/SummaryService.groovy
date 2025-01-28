package underdog.guide.spectacle.webscrapping

import groovy.transform.TupleConstructor
import underdog.spectacle.dsl.Context

import java.util.function.Function

@TupleConstructor
class SummaryService implements Function<Context, String> {
    ScrappingService scrappingService
    AIService aiService

    @Override
    String apply(Context context) {
        String url = context.param('pageURL')
        ScrappingService.PageContent page = scrappingService.extractPageContent(url)
        return aiService.extractSummary(page.title, page.content)
    }
}
