package org.allesoft.enterprise_db;

import org.springframework.context.support.ClassPathXmlApplicationContext;

public class DBApp {
    public static void main(String[] args) {
        ClassPathXmlApplicationContext applicationContext = new ClassPathXmlApplicationContext("db_queue_configuration_context.xml");
        applicationContext.refresh();
    }
}
