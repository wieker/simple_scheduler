package org.allesoft.enterprise.json_rpc;

import org.springframework.context.support.ClassPathXmlApplicationContext;

public class JsonRPCStreamServer {
    public static void main(String[] args) throws Exception {
        ClassPathXmlApplicationContext applicationContext = new ClassPathXmlApplicationContext("tcp_rpc_server_context.xml");
        applicationContext.refresh();
    }
}
