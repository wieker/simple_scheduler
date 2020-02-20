package org.allesoft.simple_scheduler;

import org.allesoft.simple_scheduler.scan.ScanComponent;
import org.springframework.context.ApplicationContext;
import org.springframework.context.annotation.AnnotationConfigApplicationContext;

public class SpringMain {
    public static void main(String[] args) {
        AnnotationConfigApplicationContext context = new AnnotationConfigApplicationContext();
        context.scan("org.allesoft.simple_scheduler.scan");
        context.refresh();
        ((ScanComponent)context.getBean("scanComponent")).hello();
    }
}
