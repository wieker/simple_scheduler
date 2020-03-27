package org.allesoft.enterprise;

import org.eclipse.jetty.server.Server;
import org.eclipse.jetty.servlet.ServletContextHandler;
import org.eclipse.jetty.servlet.ServletHolder;
import org.springframework.web.context.support.AnnotationConfigWebApplicationContext;
import org.springframework.web.servlet.DispatcherServlet;

public class AsyncServer {
    public static void main(String[] args) throws Exception {
        Server server = new Server(8081);

        server.setHandler(getContext());

        server.start();
        server.join();
    }

    private static ServletContextHandler getContext() {
        AnnotationConfigWebApplicationContext webApplicationContext = new AnnotationConfigWebApplicationContext();
        webApplicationContext.scan("org.allesoft.enterprise.async");

        ServletContextHandler contextHandler = new ServletContextHandler(ServletContextHandler.SESSIONS);
        contextHandler.addServlet(new ServletHolder("spring", new DispatcherServlet(webApplicationContext)), "/");
        return contextHandler;
    }
}
