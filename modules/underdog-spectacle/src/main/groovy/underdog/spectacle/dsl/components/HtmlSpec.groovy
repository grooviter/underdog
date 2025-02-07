package underdog.spectacle.dsl.components

import groovy.transform.NamedParam
import groovy.transform.NamedVariant
import underdog.spectacle.dsl.HtmlContainer
import underdog.spectacle.dsl.HtmlElement
import underdog.spectacle.dsl.Utils

/**
 * Custom container which builds a specific layout. This layout tries to reduce to the minimum the verbosity
 * when building a basic UI. It has three blocks:
 *
 * - inputs
 * - outputs
 * - examples
 *
 * Inputs are the html elements used for entering input data. Outputs are the elements showing the result of
 * processing the input data. Finally examples is a list of maps representing the possible input values
 * that a user can enter in the input fields.
 *
 * Apart from that the spec shows two action buttons, one for resetting the form and another for triggering
 * the form.
 *
 * @since 0.1.0
 */
class HtmlSpec extends HtmlContainer {
    List<HtmlElement> inputList = []
    List<HtmlElement> outputList = []
    HtmlDataFrame exampleList
    Closure onSubmitClosure
    Boolean onSubmitStreaming

    /**
     * Contains all input fields of the form
     *
     * @param closure dsl for adding input elements
     * @since 0.1.0
     */
    void inputs(@DelegatesTo(HtmlContainer) Closure closure) {
        HtmlContainer inputsContainer = new HtmlRow(application: this.application, parent: this).tap(closure)
        this.inputList.addAll(inputsContainer.children)
    }

    /**
     * Contains all output fields of the form
     *
     * @param closure dsl for adding input elements
     * @since 0.1.0
     */
    void outputs(@DelegatesTo(HtmlContainer) Closure closure) {
        HtmlContainer outputSpec = new HtmlRow(application: this.application, parent: this).tap(closure)
        this.outputList.addAll(outputSpec.children)
    }

    /**
     * Adds examples (maps of values) to the spec. This will eventually render a table with the
     * values passed as parameter
     *
     * @param list list of examples, this should be a list of type List<Map<String,?>>
     * @since 0.1.0
     */
    void setExamples(List<Map<String,?>> list) {
        exampleList = new HtmlDataFrame(value: list.toDataFrame("examples"))
    }

    @NamedVariant
    void onSubmit(
        @NamedParam(required = false) Boolean streaming = false,
        Closure onSubmitClosure
    ) {
        this.onSubmitStreaming = streaming
        this.onSubmitClosure = onSubmitClosure
    }

    HtmlSpec initLayout() {
        def formName = Utils.generateRandomName()
        return this.tap {
                row {
                    col {
                        form(
                            streaming: this.onSubmitStreaming,
                            name: formName,
                            indicatorSelector: """\
                            | #${formName} input,
                            | #${formName} textarea,
                            | #${formName} .form-check-label,
                            | #${formName} button,
                            | #spec-progress""".stripMargin().stripIndent()
                        ) {
                            card {
                                cardBody {
                                    inputList.each(delegate::addChild)
                                }
                                cardFooter {
                                    div(className: 'd-flex') {
                                        resetLink(text: 'Reset')
                                        button(
                                            text: 'Run',
                                            iconName: 'bi bi-easel',
                                            className: 'btn btn-outline-primary ms-auto'
                                        )
                                    }
                                }
                            }
                            onSubmit(
                                inputList.collect { it.name },
                                outputList.collect { it.name },
                                onSubmitClosure
                            )
                        }
                    }
                    col {
                        card {
                            cardBody {
                                progressIndeterminate(name: 'spec-progress', className: "+mb-3")
                                outputList.each(delegate::addChild)
                            }
                        }
                    }
                }
                if (exampleList) {
                    row {
                        col {
                            card {
                                cardHeader {
                                    markdown "### Examples"
                                }
                                div(className: 'table-responsive'){
                                    addChild(exampleList)
                                }
                            }
                        }
                    }
                } else {
                    row {}
                }
        }
    }
}
