package underdog.spectacle.templates

import groovy.text.Template
import groovy.text.markup.MarkupTemplateEngine
import groovy.text.markup.TemplateConfiguration
import org.commonmark.node.Node
import org.commonmark.parser.Parser
import org.commonmark.renderer.html.HtmlRenderer
import underdog.spectacle.dsl.HtmlContainer
import underdog.spectacle.dsl.HtmlElement
import underdog.spectacle.dsl.components.HtmlMarkdown

/**
 * Class responsible for rendering DSL elements to html code
 *
 * @since 0.1.0
 */
class TemplateEngine {

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
        return renderer.render(document)
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
        TemplateConfiguration config = new TemplateConfiguration();
        MarkupTemplateEngine engine = new MarkupTemplateEngine(config);
        URL templatePath = this.class.classLoader.getResource("templates/${templateName}.tpl")
        Template template = engine.createTemplate(templatePath)
        Writable output = template.make(model);
        StringWriter writer = new StringWriter() // TODO: use response writer
        output.writeTo(writer);
        return writer.toString()
    }
}
