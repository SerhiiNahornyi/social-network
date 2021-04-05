package com.kpi.project.resource;

import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
public class TestController {

    @RequestMapping("/test/string")
    public String test() {
        return "string.ok";
    }

    @RequestMapping("/test/error")
    public Object illegalArgument() {
        throw new IllegalArgumentException("some exception");
    }

    @RequestMapping("/test/error2")
    public Object exception() throws Exception {
        throw new Exception("some exception");
    }
}
