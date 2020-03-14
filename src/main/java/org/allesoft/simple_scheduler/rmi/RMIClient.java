package org.allesoft.simple_scheduler.rmi;

import org.springframework.context.support.ClassPathXmlApplicationContext;

public class RMIClient {
    public static void main(String[] args) {
        ClassPathXmlApplicationContext applicationContext = new ClassPathXmlApplicationContext("rmi_client_context.xml");
        applicationContext.refresh();
        ClientServiceImpl clientService = (ClientServiceImpl) applicationContext.getBean("clientService");
        clientService.message();
    }
}
