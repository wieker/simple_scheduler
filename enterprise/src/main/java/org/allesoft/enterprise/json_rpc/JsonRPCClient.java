package org.allesoft.enterprise.json_rpc;

import org.springframework.context.support.ClassPathXmlApplicationContext;

public class JsonRPCClient {
    public static void main(String[] args) {

        ClassPathXmlApplicationContext applicationContext = new ClassPathXmlApplicationContext("json_rpc_client_context.xml");
        applicationContext.refresh();
        Hello clientService = (Hello) applicationContext.getBean("hello");
        clientService.message();
    }
}
