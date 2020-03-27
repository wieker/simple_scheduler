package org.allesoft.enterprise;

import org.apache.catalina.Context;
import org.apache.catalina.startup.Tomcat;
import org.springframework.beans.BeansException;
import org.springframework.beans.factory.BeanFactory;
import org.springframework.beans.factory.NoSuchBeanDefinitionException;
import org.springframework.beans.factory.ObjectProvider;
import org.springframework.beans.factory.config.AutowireCapableBeanFactory;
import org.springframework.context.ApplicationContext;
import org.springframework.context.MessageSourceResolvable;
import org.springframework.context.NoSuchMessageException;
import org.springframework.core.ResolvableType;
import org.springframework.core.env.Environment;
import org.springframework.core.io.Resource;
import org.springframework.web.context.WebApplicationContext;
import org.springframework.web.servlet.DispatcherServlet;
import org.springframework.web.servlet.HandlerMapping;
import org.springframework.web.servlet.mvc.method.annotation.RequestMappingHandlerMapping;

import javax.servlet.ServletContext;
import javax.servlet.ServletException;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.File;
import java.io.IOException;
import java.io.Writer;
import java.lang.annotation.Annotation;
import java.util.HashMap;
import java.util.Locale;
import java.util.Map;

public class SimpleContextServer {
    public static void main(String[] args) throws Exception {
        Tomcat tomcat = new Tomcat();
        tomcat.setPort(8081);
        tomcat.getConnector();

        Context ctx = tomcat.addContext("", new File("tmp").getAbsolutePath());

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

        Map<String, HandlerMapping> beans = new HashMap<>();
        beans.put("default", new RequestMappingHandlerMapping());

        WebApplicationContext webApplicationContext = new WebApplicationContext() {
            @Override
            public ServletContext getServletContext() {
                return null;
            }

            @Override
            public String getId() {
                return null;
            }

            @Override
            public String getApplicationName() {
                return null;
            }

            @Override
            public String getDisplayName() {
                return null;
            }

            @Override
            public long getStartupDate() {
                return 0;
            }

            @Override
            public ApplicationContext getParent() {
                return null;
            }

            @Override
            public AutowireCapableBeanFactory getAutowireCapableBeanFactory() throws IllegalStateException {
                return null;
            }

            @Override
            public BeanFactory getParentBeanFactory() {
                return null;
            }

            @Override
            public boolean containsLocalBean(String s) {
                return false;
            }

            @Override
            public boolean containsBeanDefinition(String s) {
                return false;
            }

            @Override
            public int getBeanDefinitionCount() {
                return 0;
            }

            @Override
            public String[] getBeanDefinitionNames() {
                return new String[0];
            }

            @Override
            public String[] getBeanNamesForType(ResolvableType resolvableType) {
                return new String[0];
            }

            @Override
            public String[] getBeanNamesForType(ResolvableType resolvableType, boolean b, boolean b1) {
                return new String[0];
            }

            @Override
            public String[] getBeanNamesForType(Class<?> aClass) {
                return new String[0];
            }

            @Override
            public String[] getBeanNamesForType(Class<?> aClass, boolean b, boolean b1) {
                return new String[0];
            }

            @Override
            public <T> Map<String, T> getBeansOfType(Class<T> aClass) throws BeansException {
                return null;
            }

            @Override
            public <T> Map<String, T> getBeansOfType(Class<T> aClass, boolean b, boolean b1) throws BeansException {
                return (Map<String, T>) beans;
            }

            @Override
            public String[] getBeanNamesForAnnotation(Class<? extends Annotation> aClass) {
                return new String[0];
            }

            @Override
            public Map<String, Object> getBeansWithAnnotation(Class<? extends Annotation> aClass) throws BeansException {
                return null;
            }

            @Override
            public <A extends Annotation> A findAnnotationOnBean(String s, Class<A> aClass) throws NoSuchBeanDefinitionException {
                return null;
            }

            @Override
            public Object getBean(String s) throws BeansException {
                return null;
            }

            @Override
            public <T> T getBean(String s, Class<T> aClass) throws BeansException {
                return null;
            }

            @Override
            public Object getBean(String s, Object... objects) throws BeansException {
                return null;
            }

            @Override
            public <T> T getBean(Class<T> aClass) throws BeansException {
                return null;
            }

            @Override
            public <T> T getBean(Class<T> aClass, Object... objects) throws BeansException {
                return null;
            }

            @Override
            public <T> ObjectProvider<T> getBeanProvider(Class<T> aClass) {
                return null;
            }

            @Override
            public <T> ObjectProvider<T> getBeanProvider(ResolvableType resolvableType) {
                return null;
            }

            @Override
            public boolean containsBean(String s) {
                return false;
            }

            @Override
            public boolean isSingleton(String s) throws NoSuchBeanDefinitionException {
                return false;
            }

            @Override
            public boolean isPrototype(String s) throws NoSuchBeanDefinitionException {
                return false;
            }

            @Override
            public boolean isTypeMatch(String s, ResolvableType resolvableType) throws NoSuchBeanDefinitionException {
                return false;
            }

            @Override
            public boolean isTypeMatch(String s, Class<?> aClass) throws NoSuchBeanDefinitionException {
                return false;
            }

            @Override
            public Class<?> getType(String s) throws NoSuchBeanDefinitionException {
                return null;
            }

            @Override
            public Class<?> getType(String s, boolean b) throws NoSuchBeanDefinitionException {
                return null;
            }

            @Override
            public String[] getAliases(String s) {
                return new String[0];
            }

            @Override
            public void publishEvent(Object event) {

            }

            @Override
            public String getMessage(String code, Object[] args, String defaultMessage, Locale locale) {
                return null;
            }

            @Override
            public String getMessage(String code, Object[] args, Locale locale) throws NoSuchMessageException {
                return null;
            }

            @Override
            public String getMessage(MessageSourceResolvable resolvable, Locale locale) throws NoSuchMessageException {
                return null;
            }

            @Override
            public Environment getEnvironment() {
                return null;
            }

            @Override
            public Resource[] getResources(String s) throws IOException {
                return new Resource[0];
            }

            @Override
            public Resource getResource(String s) {
                return null;
            }

            @Override
            public ClassLoader getClassLoader() {
                return null;
            }
        };
        Tomcat.addServlet(ctx, "spring", new DispatcherServlet(webApplicationContext));
        ctx.addServletMappingDecoded("/", "spring");

        tomcat.start();
        tomcat.getServer().await();
    }
}
