package underdog.spectacle.jetty

import org.eclipse.jetty.http.HttpHeader
import org.eclipse.jetty.io.Content
import org.eclipse.jetty.server.Request
import org.eclipse.jetty.server.Response
import org.eclipse.jetty.util.Callback
import underdog.spectacle.dsl.Controller


class POSTHandler extends RESTHandler {
    POSTHandler(Controller controller) {
        super(controller)
    }

    @Override
    protected boolean onConditionsMet(Request request, Response response, Callback callback) throws Exception {
        def fn = this.controller.function
        def result = fn(request, response)

        response.headers.add(HttpHeader.CONTENT_TYPE, "application/json")
        Content.Sink.write(response, true, result.toString(), callback)
        return true
    }
}
