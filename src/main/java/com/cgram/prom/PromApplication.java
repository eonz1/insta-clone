package com.cgram.prom;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.data.jpa.repository.config.EnableJpaAuditing;

@EnableJpaAuditing
@SpringBootApplication
public class PromApplication {

    public static void main(String[] args) {
        SpringApplication.run(PromApplication.class, args);
    }

}
