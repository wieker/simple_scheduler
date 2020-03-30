package org.allesoft.enterprise.tomcat;

import org.apache.catalina.Context;
import org.apache.catalina.LifecycleException;
import org.apache.catalina.startup.Tomcat;
import org.springframework.beans.BeansException;
import org.springframework.context.ApplicationContext;
import org.springframework.context.ApplicationContextAware;
import org.springframework.web.context.support.AnnotationConfigWebApplicationContext;
import org.springframework.web.servlet.DispatcherServlet;

import java.io.File;

public class TomcatStarterImpl implements ApplicationContextAware, TomcatStarter {
    ApplicationContext applicationContext;
    private Integer tomcatPort;

    public TomcatStarterImpl(Integer tomcatPort) {
        this.tomcatPort = tomcatPort;
    }

    @Override
    public void startEmbedded() {
        Tomcat tomcat = new Tomcat();
        tomcat.setPort(tomcatPort);
        tomcat.getConnector();

        Context ctx = tomcat.addContext("", new File(".").getAbsolutePath());

        AnnotationConfigWebApplicationContext webApplicationContext = new AnnotationConfigWebApplicationContext();
        webApplicationContext.scan("org.example.test_task.controller");
        webApplicationContext.setParent(applicationContext);
        Tomcat.addServlet(ctx, "spring", new DispatcherServlet(webApplicationContext));
        ctx.addServletMappingDecoded("/", "spring");

        try {
            tomcat.start();
        } catch (LifecycleException e) {
            throw new RuntimeException(e);
        }
    }

    @Override
    public void setApplicationContext(ApplicationContext applicationContext) throws BeansException {
        this.applicationContext = applicationContext;
    }
}
