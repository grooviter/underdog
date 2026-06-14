package underdog.spectacle.templates

import groovy.text.Template
import groovy.text.TemplateEngine
import groovy.text.markup.MarkupTemplateEngine
import groovy.text.markup.TemplateConfiguration
import groovy.util.logging.Slf4j
import org.commonmark.node.Node
import org.commonmark.parser.Parser
import org.commonmark.renderer.html.HtmlRenderer
import underdog.spectacle.dsl.HtmlContainer
import underdog.spectacle.dsl.HtmlElement
import underdog.spectacle.dsl.HtmlPage
import underdog.spectacle.dsl.components.HtmlButton
import underdog.spectacle.dsl.components.HtmlCard
import underdog.spectacle.dsl.components.HtmlCardBody
import underdog.spectacle.dsl.components.HtmlCardFooter
import underdog.spectacle.dsl.components.HtmlCardHeader
import underdog.spectacle.dsl.components.HtmlColumn
import underdog.spectacle.dsl.components.HtmlDiv
import underdog.spectacle.dsl.components.HtmlForm
import underdog.spectacle.dsl.components.HtmlMarkdown
import underdog.spectacle.dsl.HtmlNavigation
import underdog.spectacle.dsl.components.HtmlRow
import underdog.spectacle.dsl.components.HtmlSpec

/**
 * Class responsible for rendering DSL elements to html code
 *
 * @since 0.1.0
 */
@Slf4j
class CachedTemplateEngine {
    static class TemplateCacheEntry {
        final Date date
        long hit = 0
        long lastModified
        long length
        final Template template

        TemplateCacheEntry(File file, Template template, boolean timestamp) {
            if (!template) {
                throw new NullPointerException("template")
            }
            if (timestamp) {
                this.date = new Date(System.currentTimeMillis())
            }
            if (file) {
                this.lastModified = file.lastModified()
                this.length = file.length()
            }
            this.template = template
        }

        /**
         * Checks the passed file attributes against those cached ones.
         *
         * @param file Other file handle to compare to the cached values. May be null in which case the validation is skipped.
         * @return <code>true</code> if all measured values match, else <code>false</code>
         */
        boolean validate(File file) {
            if (file) {
                if (file.lastModified() != this.lastModified) {
                    return false
                }
                if (file.length() != this.length) {
                    return false
                }
            }
            hit++
            return true
        }

        @Override
        String toString() {
            return !date ? "Hit #$hit" : "Hit #$hit since $date"
        }
    }

    private static final String GROOVY_SOURCE_ENCODING = "groovy.source.encoding"

    private static final List<Class> PRE_CACHED_TEMPLATE_CLASSES = [
            HtmlPage,
            HtmlRow,
            HtmlColumn,
            HtmlSpec,
            HtmlForm,
            HtmlCard,
            HtmlCardBody,
            HtmlCardHeader,
            HtmlCardFooter,
            HtmlButton,
            HtmlNavigation,
            HtmlMarkdown
    ]

    /**
     * Simple file name to template cache map.
     *
     * @since 0.1.0
     */
    private final Map<String, TemplateCacheEntry> cache = new HashMap<String, TemplateCacheEntry>()

    /**
     * Underlying template engine used to evaluate template source files.
     *
     * @since 0.1.0
     */
    private TemplateEngine engine = new MarkupTemplateEngine(new TemplateConfiguration())

    /**
     * @since 0.1.0
     */
    private String fileEncodingParamVal

    void cacheBase() {
        PRE_CACHED_TEMPLATE_CLASSES.each {
            URL templateURL = this.class.classLoader.getResource("templates/${it.simpleName}.tpl")
            getTemplate(templateURL)
            log.debug("template ${it.simpleName} pre-cached")
        }
    }

    String render(HtmlPage htmlPage) {
        def container = htmlPage

        if (isThereAnyStreamingEvent(htmlPage)) {
            container.children = [getChildrenWhenStreaming(htmlPage)]
        }

        String navigationContent = ""

        if (htmlPage.application.shouldShowPagination()) {
            container.htmlNavigation.page = htmlPage
            navigationContent = render(container.htmlNavigation)
        }

        String childrenContent = container.children
            .collect { render(it) }
            .join("\n")

        return executeTemplate(
            container.class.simpleName,
            [element: container, navigation: navigationContent, childrenContent: childrenContent]
        )
    }

    private static isThereAnyStreamingEvent(HtmlPage htmlPage) {
        return htmlPage.application.eventList.any { it.isStreaming() }
    }

    private static HtmlDiv getChildrenWhenStreaming(HtmlPage htmlPage) {
        HtmlDiv root = new HtmlDiv()
        HtmlDiv body = htmlPage
            .eventList
            .findAll { it.isStreaming() }
            .inject(root){ agg, val ->
                return new HtmlDiv().tap {
                    agg.addChild(it)
                    extraAttributes = ['ws-connect': val.path, 'hx-ext': 'ws']
                }
            }
        body.children = htmlPage.children
        return root
    }

    /**
     * Renders a {@link HtmlContainer}
     *
     * @param container the container to render
     * @return the html code result
     * @since 0.1.0
     */
    String render(HtmlContainer container) {
        String childrenContent = container.children
            .collect { render(it) }
            .join("\n")

        return executeTemplate(container.class.simpleName, [element: container, childrenContent: childrenContent])
    }

    /**
     * Renders a {@link HtmlElement}
     *
     * @param element instance of type {@link HtmlElement}
     * @return the html code result
     * @since 0.1.0
     */
    String render(HtmlElement element) {
        return executeTemplate(element.class.simpleName, [element: element])
    }

    /**
     * Renders a {@link HtmlMarkdown}
     *
     * @param markdown instance of type {@link HtmlMarkdown}
     * @return the html code result
     * @since 0.1.0
     */
    String render(HtmlMarkdown markdown) {
        Parser parser = Parser.builder().build()
        Node document = parser.parse(markdown.value.stripMargin().stripIndent())
        HtmlRenderer renderer = HtmlRenderer.builder().build()
        return executeTemplate(markdown.class.simpleName, [element: markdown, markdown: renderer.render(document)])
    }

    /**
     * Executes a template with an associated data or model and returns a string with the html code
     *
     * @param templateName the name of the template, corresponds normally to the element class simple name
     * @param model data used to complete the rendering
     * @return the html code result
     * @since 0.1.0
     */
    String executeTemplate(String templateName, Map<String,Object> model) {
        URL templateURL = this.class.classLoader.getResource("templates/${templateName}.tpl")
        Template template = getTemplate(templateURL)
        Writable output = template.make(model)
        StringWriter writer = new StringWriter() // TODO: use response writer
        output.writeTo(writer)
        return writer.toString()
    }

    /**
     * Find a cached template for a given key. If a <code>File</code> is passed then
     * any cached object is validated against the File to determine if it is out of
     * date
     * @param key a unique key for the template, such as a file's absolutePath or a URL.
     * @param file a file to be used to determine if the cached template is stale. May be null.
     * @return The cached template, or null if there was no cached entry, or the entry was stale.
     */
    private Template findCachedTemplate(String key, File file) {
        Template template = null
        log.debug("Looking for cached template by key $key")
        TemplateCacheEntry entry = (TemplateCacheEntry) cache.get(key)
        if (entry) {
            if (entry.validate(file)) {
                log.debug("Cache hit! $entry")
                template = entry.template
            } else {
                log.debug("Cached template $key needs recompilation! $entry")
            }
        } else {
            log.debug("Cache miss for " + key)
        }

        return template
    }

    /**
     * Compile the template and store it in the cache.
     * @param key a unique key for the template, such as a file's absolutePath or a URL.
     * @param inputStream an InputStream for the template's source.
     * @param file a file to be used to determine if the cached template is stale. May be null.
     * @return the created template.
     */
    private Template createAndStoreTemplate(String key, InputStream inputStream, File file) {
        log.debug("Creating new template from " + key + "...")
        Reader reader = null

        try {
            String fileEncoding = (fileEncodingParamVal != null)
                ? fileEncodingParamVal
                : System.getProperty(GROOVY_SOURCE_ENCODING)

            reader = fileEncoding == null
                ? new InputStreamReader(inputStream)
                : new InputStreamReader(inputStream, fileEncoding)

            Template template = engine.createTemplate(reader)
            cache.put(key, new TemplateCacheEntry(file, template, true))
            log.debug("Created and added template to cache. [key=$key] ${cache.get(key)}")

            if (template == null) {
                throw new RuntimeException("Template is null? Should not happen here!")
            }

            return template
        } finally {
            if (reader != null) {
                reader.close()
            } else if (inputStream != null) {
                inputStream.close()
            }
        }
    }

    /**
     * Gets the template created by the underlying engine parsing the request.
     *
     * <p>
     * This method looks up a simple (weak) hash map for an existing template
     * object that matches the source URL. If there is no cache entry, a new one is
     * created by the underlying template engine. This new instance is put
     * to the cache for consecutive calls.
     *
     * @return The template that will produce the response text.
     * @param url The URL containing the template source..
     */
    protected Template getTemplate(URL url) {
        String key = url.toString()
        Template template = findCachedTemplate(key, null)
        if (!template) {
            try {
                template = createAndStoreTemplate(key, url.openConnection().getInputStream(), null)
            } catch (Exception e) {
                throw new RuntimeException("Creation of template failed: $e", e)
            }
        }
        return template
    }
}
