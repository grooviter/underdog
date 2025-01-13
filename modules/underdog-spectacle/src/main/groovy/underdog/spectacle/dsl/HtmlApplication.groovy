package underdog.spectacle.dsl

import groovy.transform.NamedParam
import groovy.transform.NamedVariant
import groovy.transform.stc.ClosureParams
import groovy.transform.stc.SimpleType


/**
 * @since 0.1.0
 */
class HtmlApplication {
    /**
     * @since 0.1.0
     */
    List<HtmlPage> htmlPageList = []

    /**
     * @since 0.1.0
     */
    List<Controller> controllerList = []

    /**
     * @since 0.1.0
     */
    Map<String,String> field = [:].<String, String>withDefault(HtmlApplication::generateRandomName)

    /**
     * @since 0.1.0
     */
    Map<String, String> function = [:].<String, String>withDefault(HtmlApplication::generateRandomName)

    /**
     * @param path
     * @param name
     * @param closure
     * @since 0.1.0
     */
    @NamedVariant
    void page(
        String path,
        @NamedParam(required = false) String name = generateRandomName(),
        @DelegatesTo(HtmlPage) Closure closure) {
        this.htmlPageList.add(new HtmlPage(path: path, name: name).tap { with(closure) })
    }

    /**
     * @param path
     * @param name
     * @param closure
     * @return
     * @since 0.1.0
     */
    @NamedVariant
    Controller post(
        String path,
        @NamedParam(required = false)
        String name = generateRandomName(),
        @DelegatesTo(ControllerUtils)
        @ClosureParams(
            value=SimpleType,
            options=['org.eclipse.jetty.server.Request', 'org.eclipse.jetty.server.Response']
        )
        Closure closure
    ) {
        def upgradedClosure = closure.tap { it.setDelegate(new ControllerUtils()) }
        return new Controller(path: path, name: name, method: 'POST', function: upgradedClosure).tap(this.controllerList::add)
    }

    private static String generateRandomName(String _unused = "") {
        return new Date().toString().md5()
    }
}
