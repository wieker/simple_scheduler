package org.allesoft.enterprise.configurable;

import org.apache.catalina.Context;
import org.apache.catalina.LifecycleException;
import org.apache.catalina.startup.Tomcat;
import org.springframework.beans.factory.InitializingBean;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.web.context.support.AnnotationConfigWebApplicationContext;
import org.springframework.web.servlet.DispatcherServlet;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.File;
import java.io.IOException;
import java.io.Writer;
import java.util.List;

@Configuration
public class TomcatConfiguration implements InitializingBean {
    private Tomcat tomcat;
    private Context context;

    @Autowired
    public void setTomcat(Tomcat tomcat) {
        this.tomcat = tomcat;
    }

    @Autowired
    public void setContext(Context context) {
        this.context = context;
    }

    @Bean
    public Tomcat tomcat() {
        Tomcat tomcat = new Tomcat();
        tomcat.setPort(8081);
        tomcat.getConnector();
        return tomcat;
    }

    @Bean
    public Context configuredContext(Tomcat tomcat, List<ServletConfig> servletConfig) {
        Context ctx = tomcat.addContext("", new File("tmp").getAbsolutePath());
        for (ServletConfig config : servletConfig) {
            Tomcat.addServlet(ctx, config.getName(), config.getServlet());
            ctx.addServletMappingDecoded(config.getPath(), config.getName());
        }
        return ctx;
    }

    @Bean
    public DispatcherServlet dispatcherServlet() {
        AnnotationConfigWebApplicationContext webApplicationContext = new AnnotationConfigWebApplicationContext();
        webApplicationContext.scan("org.allesoft.enterprise.configurable.controller");
        return new DispatcherServlet(webApplicationContext);
    }

    @Bean
    public HttpServlet helloServlet() {
        return new HttpServlet() {
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
        };
    }

    @Override
    public void afterPropertiesSet() throws Exception {
        finalStep();
    }

    private void finalStep() throws LifecycleException {
        tomcat.start();
        tomcat.getServer().await();
    }
}
