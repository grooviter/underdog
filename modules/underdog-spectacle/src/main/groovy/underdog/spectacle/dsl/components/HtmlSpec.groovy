package underdog.spectacle.dsl.components

import underdog.spectacle.dsl.HtmlContainer
import underdog.spectacle.dsl.HtmlElement
import underdog.spectacle.dsl.HtmlPage

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

    void onSubmit(Closure onSubmitClosure) {
        this.onSubmitClosure = onSubmitClosure
    }

    HtmlSpec initLayout() {
        return this.tap {
            form {
                row {
                    col {
                        card {
                            cardBody{
                                inputList.each(delegate::addChild)
                            }
                            cardFooter {
                                div(className: 'd-flex') {
                                    resetLink(text: 'Reset')
                                    button(text: 'Run', className: 'btn btn-outline-primary ms-auto')
                                }
                            }
                        }
                    }
                    col {
                        card(className: "+h-100") {
                            cardBody {
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
                onSubmit(
                        inputList.collect { it.name },
                        outputList.collect { it.name },
                        onSubmitClosure
                )
            }
        }
    }
}
