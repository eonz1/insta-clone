package com.cgram.prom.infra.mail.service;

import com.cgram.prom.infra.mail.model.MailRequest;
import com.cgram.prom.infra.mail.model.NcloudApiProperties;
import com.cgram.prom.infra.mail.model.NcloudMailResponse;
import java.nio.charset.StandardCharsets;
import java.security.InvalidKeyException;
import java.security.NoSuchAlgorithmException;
import java.time.Instant;
import java.util.Base64;
import javax.crypto.Mac;
import javax.crypto.spec.SecretKeySpec;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpHeaders;
import org.springframework.http.MediaType;
import org.springframework.stereotype.Component;
import org.springframework.web.reactive.function.client.WebClient;

@Slf4j
@RequiredArgsConstructor
@Component
public class NcloudMail implements MailSender{

    private final NcloudApiProperties ncloudApiProperties;

    private final String domainUrl = "https://mail.apigw.ntruss.com";

    private final String emailSendApiUrl = "/api/v1/mails";

    private final WebClient webClient = WebClient.builder()
                                                    .baseUrl(domainUrl)
                                                    .defaultHeader(HttpHeaders.CONTENT_TYPE, MediaType.APPLICATION_JSON_VALUE)
                                                    .build();

    public String makeSignature(String apiUrl, String timestamp) {

        String space = " ";  // 공백
        String newLine = "\n";  // 줄바꿈
        String method = "POST";  // HTTP 메소드
        String accessKey = ncloudApiProperties.getAccessKeyId();
        String secretKey = ncloudApiProperties.getSecretAccessKey();

        String message = method +
            space +
            apiUrl +
            newLine +
            timestamp +
            newLine +
            accessKey;

        SecretKeySpec signingKey = new SecretKeySpec(secretKey.getBytes(StandardCharsets.UTF_8), "HmacSHA256");

        Mac mac;
        try {
            mac = Mac.getInstance("HmacSHA256");
            mac.init(signingKey);
        } catch (NoSuchAlgorithmException | InvalidKeyException e) {
            throw new RuntimeException(e);
        }

        byte[] rawHmac = mac.doFinal(message.getBytes(StandardCharsets.UTF_8));

        return Base64.getEncoder().encodeToString(rawHmac);
    }

    @Override
    public void send(MailRequest mailRequest) {
        NcloudMailResponse response = webClient.post().uri(uriBuilder -> uriBuilder.path(emailSendApiUrl).build())
                                                            .headers(this::addXNcpHeaderInfo)
                                                            .bodyValue(mailRequest)
                                                            .retrieve()
                                                            .bodyToMono(NcloudMailResponse.class)
                                                            .block();

        log.debug("" + response.toString());
    }
    private void addXNcpHeaderInfo(HttpHeaders headers) {
        String timestamp = String.valueOf(Instant.now().toEpochMilli()); // "1664506837308";  // 현재 타임스탬프 (epoch, millisecond)
        headers.add("x-ncp-apigw-timestamp", String.valueOf(Instant.now().toEpochMilli()));
        headers.add("x-ncp-iam-access-key", ncloudApiProperties.getAccessKeyId());
        headers.add("x-ncp-apigw-signature-v2", makeSignature(emailSendApiUrl, timestamp));
    }
}