package org.allesoft.simple_scheduler.scan;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

@Component
public class ScanComponent {
    @Autowired
    ScanComponent component;

    public void hello() {
        System.out.println("Hello");
    }
}
