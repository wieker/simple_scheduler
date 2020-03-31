package org.allesoft.enterprise;

import org.apache.catalina.Context;
import org.apache.catalina.startup.Tomcat;
import org.springframework.http.converter.json.MappingJackson2HttpMessageConverter;
import org.springframework.web.context.support.AnnotationConfigWebApplicationContext;
import org.springframework.web.servlet.DispatcherServlet;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.File;
import java.io.IOException;
import java.io.Writer;

public class SimpleServer {
    public static void main(String[] args) throws Exception {
        Tomcat tomcat = new Tomcat();
        tomcat.setPort(8081);
        tomcat.getConnector();

        Context ctx = tomcat.addContext("", new File("d2").getAbsolutePath());

        Tomcat.addServlet(ctx, "hello", new HttpServlet() {
            private static final long serialVersionUID = 3600060857537422698L;

            @Override
            protected void service(HttpServletRequest request, HttpServletResponse response)
                    throws ServletException, IOException {
                response.setCharacterEncoding("UTF-8");
                response.setContentType("text/plain");
                try (Writer writer = response.getWriter()) {
                    writer.write("Hello, Embedded World from Blue Lotus Software!");
                    writer.flush();
                }
            }
        });
        ctx.addServletMappingDecoded("/hello", "hello");

        AnnotationConfigWebApplicationContext webApplicationContext = new AnnotationConfigWebApplicationContext();
        webApplicationContext.register(MappingJackson2HttpMessageConverter.class);
        webApplicationContext.scan("org.allesoft.enterprise.configurable.controller");
        Tomcat.addServlet(ctx, "spring", new DispatcherServlet(webApplicationContext));
        ctx.addServletMappingDecoded("/", "spring");

        tomcat.start();
        tomcat.getServer().await();
    }
}
