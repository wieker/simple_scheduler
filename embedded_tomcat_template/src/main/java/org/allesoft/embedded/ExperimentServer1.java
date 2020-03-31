package org.allesoft.embedded;

import org.apache.catalina.Context;
import org.apache.catalina.startup.Tomcat;
import org.springframework.web.context.support.AnnotationConfigWebApplicationContext;
import org.springframework.web.servlet.DispatcherServlet;

import java.io.File;

public class ExperimentServer1 {
    public static void main(String[] args) throws Exception {
        Tomcat tomcat = new Tomcat();
        tomcat.setPort(8081);
        tomcat.getConnector();

        Context ctx = tomcat.addContext("", new File("d2").getAbsolutePath());

        AnnotationConfigWebApplicationContext webApplicationContext = new AnnotationConfigWebApplicationContext();
        webApplicationContext.scan("org.allesoft.embedded.configurable");
        Tomcat.addServlet(ctx, "spring", new DispatcherServlet(webApplicationContext));
        ctx.addServletMappingDecoded("/", "spring");

        tomcat.start();
        tomcat.getServer().await();
    }
}
