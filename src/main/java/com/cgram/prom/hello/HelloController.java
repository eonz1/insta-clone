package com.cgram.prom.hello;

import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequiredArgsConstructor
@RequestMapping("/hello")
public class HelloController {

    private final HelloMapper helloMapper;

    @GetMapping("")
    public String hello() {
        return helloMapper.sayHello();
    }
}
