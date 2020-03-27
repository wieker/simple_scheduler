package org.allesoft.enterprise;

import com.kendelong.web.jmxconsole.LookupMBeansServlet;
import org.eclipse.jetty.server.Server;
import org.eclipse.jetty.servlet.ServletContextHandler;
import org.eclipse.jetty.servlet.ServletHolder;
import org.springframework.web.context.support.AnnotationConfigWebApplicationContext;
import org.springframework.web.servlet.DispatcherServlet;

import javax.management.MBeanServer;
import javax.management.ObjectName;
import java.lang.management.ManagementFactory;

public class AsyncServer {
    public static void main(String[] args) throws Exception {
        Server server = new Server(8081);

        server.setHandler(getContext());

        jmx();

        server.start();
        server.join();
    }

    private static ServletContextHandler getContext() {
        AnnotationConfigWebApplicationContext webApplicationContext = new AnnotationConfigWebApplicationContext();
        webApplicationContext.scan("org.allesoft.enterprise.async");

        ServletContextHandler contextHandler = new ServletContextHandler(ServletContextHandler.SESSIONS);
        contextHandler.addServlet(new ServletHolder("jmx", new LookupMBeansServlet()), "/jmx-console/*");
        contextHandler.addServlet(new ServletHolder("spring", new DispatcherServlet(webApplicationContext)), "/");
        return contextHandler;
    }

    private static void jmx() throws Exception {
        MBeanServer mbs = ManagementFactory.getPlatformMBeanServer();

        // Construct the ObjectName for the MBean we will register
        ObjectName name = new ObjectName("com.example.mbeans:type=Hello");

        // Create the Hello World MBean
        Hello mbean = new Hello();

        // Register the Hello World MBean
        mbs.registerMBean(mbean, name);
    }

    public static class Hello implements HelloMBean {
        @Override
        public String hello() {
            return "greeting";
        }
    }

    public static interface HelloMBean {
        String hello();
    }
}
