package com.cgram.prom.infra.mail.model;

import lombok.Getter;
import lombok.Setter;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.stereotype.Component;

@Component
@Getter
@Setter
@ConfigurationProperties(prefix = "ncloud.api")
public class NcloudApiProperties {

    private String accessKeyId;

    private String secretAccessKey;
}
