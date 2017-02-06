package com.sdadas.spring2ts.examples.simple;

import com.sdadas.spring2ts.annotations.SharedService;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import java.util.concurrent.atomic.AtomicInteger;

/**
 * @author Sławomir Dadas
 */
@CrossOrigin
@RestController
@SharedService
public class HelloController {

    private final AtomicInteger id = new AtomicInteger();

    @RequestMapping("/hello")
    public HelloResponse hello(@RequestParam(value = "name", defaultValue = "world") String name) {
        return new HelloResponse(id.incrementAndGet(), "Hello " + name);
    }
}
