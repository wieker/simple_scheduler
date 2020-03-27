package org.allesoft.enterprise;

import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RestController;

@RestController
public class SimpleController {
    @RequestMapping(value = "/check", method = RequestMethod.GET)
    public String check() {
        return "passed";
    }
}
