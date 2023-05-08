package com.cgram.prom.infra.mail.model;

import lombok.Getter;
import lombok.Setter;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.stereotype.Component;

@Component
@Getter
@Setter
@ConfigurationProperties(prefix = "letter.mail")
public class SmtpMailProperties {

    private String id;

    private String pwd;

    private String host;

    private String port;
}
