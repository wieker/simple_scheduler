package org.allesoft.simple_scheduler.scan;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.stereotype.Component;

@Component
public class ScanComponent {
    @Autowired
    ScanComponent component;
    @Autowired
    @Qualifier("qlf")
    SomeIfc ifc;

    public void hello() {
        System.out.println("Hello");
    }
}
