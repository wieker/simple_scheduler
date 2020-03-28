package org.allesoft.enterprise.configurable;

import org.springframework.context.support.ClassPathXmlApplicationContext;

public class SimpleServer {
    public static void main(String[] args) {
        ClassPathXmlApplicationContext applicationContext = new ClassPathXmlApplicationContext("tomcat_configuration_context.xml");
        applicationContext.refresh();
    }
}
