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
    private final HelloRepository helloRepository;

    @GetMapping("")
    public String hello() {
        return helloMapper.sayHello();
    }

    @GetMapping("/jpa")
    public String helloJpa() {
        Hello hello = helloRepository.findByName("hello world")
            .orElseGet(() -> new Hello(3L, "hello3"));
        return hello.getName();
    }
}
