import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.ServletException;
import java.io.IOException;
import java.util.Scanner;

import org.eclipse.jetty.server.Server;
import org.eclipse.jetty.server.Request;
import org.eclipse.jetty.server.handler.AbstractHandler;

class MockSystemUnderTest extends AbstractHandler  {
    private String cannedResponse = "";
    private String lastRequest = "No requests received yet.";
    private Server server;

    public MockSystemUnderTest(String cannedResponse) {
        this.cannedResponse = cannedResponse;
    }

    @Override
    public void handle(String target, Request baseRequest, HttpServletRequest request, HttpServletResponse response) throws IOException, ServletException {
        lastRequest = new Scanner(request.getInputStream()).useDelimiter("\\Z").next();

        response.setContentType("text/xml;charset=utf-8");
        response.setStatus(HttpServletResponse.SC_OK);
        baseRequest.setHandled(true);
        response.getWriter().print(cannedResponse);
    }

    public void startServer() throws Exception {
        server = new Server(8080);
        server.setHandler(this);
        server.start();
    }

    public void stopServer() throws Exception {
        server.stop();
    }

    public String getLastRequest() {
        return lastRequest;
    }
}