package underdog.spectacle.jetty

import org.eclipse.jetty.server.Handler
import org.eclipse.jetty.server.Request
import org.eclipse.jetty.server.Response
import org.eclipse.jetty.server.handler.ConditionalHandler.ElseNext
import org.eclipse.jetty.util.Callback
import underdog.spectacle.dsl.Controller

abstract class RESTHandler extends ElseNext {
    Controller controller

    RESTHandler(Controller controller) {
        super()
        this.controller = controller
        this.includeMethod(controller.method)
        this.includePath(controller.path)
    }

    @Override
    List<Handler> getHandlers() {
        Handler next = this.getHandler();
        return next == null ? Collections.emptyList() : Collections.singletonList(next);
    }

    @Override
    protected boolean onConditionsMet(Request request, Response response, Callback callback) throws Exception {
        return false
    }
}
