package org.allesoft.enterprise.netty;

import org.springframework.context.support.ClassPathXmlApplicationContext;

public class NettyServer {
    public static void main(String[] args) throws Exception {
        ClassPathXmlApplicationContext applicationContext = new ClassPathXmlApplicationContext("netty_server_context.xml");
        applicationContext.refresh();
    }
}

