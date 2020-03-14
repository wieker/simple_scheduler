package org.allesoft.simple_scheduler.rmi;

import org.springframework.context.support.ClassPathXmlApplicationContext;

public class RMIServer {
    public static void main(String[] args) {
        ClassPathXmlApplicationContext applicationContext = new ClassPathXmlApplicationContext("rmi_server_context.xml");
        applicationContext.refresh();
    }
}
