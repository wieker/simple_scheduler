package org.allesoft.embedded;

import org.springframework.context.support.ClassPathXmlApplicationContext;

public class DBApp {
    public static void main(String[] args) {
        ClassPathXmlApplicationContext applicationContext = new ClassPathXmlApplicationContext("jdbc_context.xml");
        applicationContext.refresh();
    }
}
