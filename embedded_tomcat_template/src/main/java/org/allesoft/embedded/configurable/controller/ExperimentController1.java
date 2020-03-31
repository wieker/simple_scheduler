package org.allesoft.embedded.configurable.controller;

import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
public class ExperimentController1 {
    @GetMapping(value = "/check")
    public JsonObject check() {
        JsonObject jsonObject = new JsonObject();
        jsonObject.setHello("hello");
        return jsonObject;
    }
}
