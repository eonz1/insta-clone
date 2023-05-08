package com.cgram.prom.domain.hello;

import jakarta.transaction.Transactional;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

@Service
@Slf4j
@RequiredArgsConstructor
public class HelloService {

    private final HelloMapper helloMapper;
    private final HelloRepository helloRepository;

    @Transactional
    public void addAndUpdateHello() {
        helloMapper.addHello();

        Hello hello = helloRepository.findByName("halo").orElseGet(() -> new Hello(4L, "없음"));
        hello.update("halo2");
    }

    @Transactional
    public String getHelloName() {
        addAndUpdateHello();

        Hello hello = helloRepository.findByName("halo2").orElseGet(() -> new Hello(4L, "없음"));
        return hello.getName();
    }
}
