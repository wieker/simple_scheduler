package org.allesoft.simple_scheduler;

import org.allesoft.simple_scheduler.scan.ScanComponent;
import org.springframework.context.ApplicationContext;
import org.springframework.context.annotation.AnnotationConfigApplicationContext;

public class SpringMain {
    public static void main(String[] args) {
        AnnotationConfigApplicationContext context = new AnnotationConfigApplicationContext();
        context.scan("org.allesoft.simple_scheduler.scan");
        context.registerBean("schedulerProcessor", SchedulerAwareBeanPostProcessor.class);
        context.refresh();
        ScanComponent scanComponent = (ScanComponent) context.getBean("scanComponent");
        scanComponent.hello();
    }
}
