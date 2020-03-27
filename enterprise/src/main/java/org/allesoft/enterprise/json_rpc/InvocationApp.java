package org.allesoft.enterprise.json_rpc;

import org.springframework.context.support.ClassPathXmlApplicationContext;

public class InvocationApp {
    public static void main(String[] args) {
        ClassPathXmlApplicationContext applicationContext = new ClassPathXmlApplicationContext("simple_interceptor.xml");
        applicationContext.refresh();
        Hello hello = (Hello) applicationContext.getBean("hello");
        hello.message();
    }
}
