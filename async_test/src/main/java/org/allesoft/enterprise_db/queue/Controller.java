package org.allesoft.enterprise_db.queue;

import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RestController;
import reactor.core.publisher.Mono;

@RestController
public class Controller {
    @RequestMapping(value = "/mono", method = RequestMethod.GET)
    public Mono<String> mono() {
        return Mono.just("hello");
    }
}
