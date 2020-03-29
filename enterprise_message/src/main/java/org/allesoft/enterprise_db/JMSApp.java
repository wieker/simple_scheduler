package org.allesoft.enterprise_db;

import org.springframework.context.support.ClassPathXmlApplicationContext;

import javax.jms.Message;
import java.util.Iterator;
import java.util.List;

public class JMSApp {
    public static void main(String[] args) throws Exception {
        ClassPathXmlApplicationContext applicationContext = new ClassPathXmlApplicationContext("jms_configuration_context.xml");
        applicationContext.refresh();

        SpringConsumer consumer = (SpringConsumer) applicationContext.getBean("consumer");
        consumer.start();

        // Wait a little to drain any left over messages.
        Thread.sleep(1000);
        consumer.flushMessages();

        SpringProducer producer = (SpringProducer) applicationContext.getBean("producer");
        producer.start();

        // lets sleep a little to give the JMS time to dispatch stuff
        consumer.waitForMessagesToArrive(producer.getMessageCount());

        // now lets check that the consumer has received some messages
        List<Message> messages = consumer.flushMessages();
        for (Iterator<Message> iter = messages.iterator(); iter.hasNext();) {
            Object message = iter.next();
            System.out.println("Received: " + message);
        }
    }
}
