package underdog.guide.spectacle.webscrapping

import org.jsoup.Jsoup
import org.jsoup.nodes.Document

class ScrappingService {
    static final USER_AGENT = "Mozilla/5.0 (Windows NT 10.0; Win64; x64) AppleWebKit/537.36 (KHTML, like Gecko) Chrome/117.0.0.0 Safari/537.36"

    static class PageContent {
        String title, content
    }

    PageContent extractPageContent(String url) {
        Document document = Jsoup.connect(url).userAgent(USER_AGENT).get()
        document.select("script style img input").remove()
        return new PageContent(title: document.title(), content: document.select("body").text())
    }
}
