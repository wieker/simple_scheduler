package org.allesoft.enterprise.json_rpc;

import org.apache.catalina.Context;
import org.apache.catalina.startup.Tomcat;
import org.springframework.web.context.support.XmlWebApplicationContext;
import org.springframework.web.servlet.DispatcherServlet;

import java.io.File;

public class JsonRPCServer {
    public static void main(String[] args) throws Exception {
        Tomcat tomcat = new Tomcat();
        tomcat.setPort(8081);
        tomcat.getConnector();
        Context ctx = tomcat.addContext("", new File("tmp").getAbsolutePath());

        XmlWebApplicationContext applicationContext = new XmlWebApplicationContext();
        applicationContext.setConfigLocation("json_rpc_server_context.xml");
        Tomcat.addServlet(ctx, "spring", new DispatcherServlet(applicationContext));
        ctx.addServletMappingDecoded("/", "spring");

        tomcat.start();
        tomcat.getServer().await();
    }
}
