package com.cgram.prom.tempmail.ncloud.service;

import com.cgram.prom.tempmail.ncloud.model.FileUploadResponse;
import com.cgram.prom.tempmail.ncloud.model.MailRequest;
import org.apache.commons.lang3.tuple.Pair;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.core.ParameterizedTypeReference;
import org.springframework.core.io.FileSystemResource;
import org.springframework.http.*;
import org.springframework.stereotype.Service;
import org.springframework.util.LinkedMultiValueMap;
import org.springframework.util.MultiValueMap;
import org.springframework.web.client.RestTemplate;

//import javax.annotation.Resource;
import javax.crypto.Mac;
import javax.crypto.spec.SecretKeySpec;
//import javax.validation.constraints.NotNull;
import java.io.File;
import java.io.UnsupportedEncodingException;
import java.net.URLEncoder;
import java.nio.charset.StandardCharsets;
import java.security.InvalidKeyException;
import java.security.NoSuchAlgorithmException;
import java.time.Instant;
import java.util.Base64;
import java.util.List;
import java.util.Map;

@Service
public class MailBizImpl implements MailBiz {

    private static final Logger LOGGER = LoggerFactory.getLogger(MailBiz.class);

    private final RestTemplate restTemplate;

//    @NotNull
//    @Value("#{resources['aws.api.accessKeyId']}")
    private String accessKeyId = "aX7FGPcDWW7JPz573Vbt";

//    @NotNull
//    @Value("#{resources['aws.api.secretAccessKey']}")
    private String secretAccessKey = "84pptACUshMqRIiLlwESMIlne0avCIKzJhFMkqAs";

    private final String domainUrl = "https://mail.apigw.ntruss.com";

    private final String emailSendApiUrl = "/api/v1/mails";

    private final String fileUploadApiUrl = "/api/v1/files";

    public MailBizImpl(RestTemplate restTemplate) {
        this.restTemplate = restTemplate;
    }

    public String makeSignature(String apiUrl, String timestamp) {

        String space = " ";  // 공백
        String newLine = "\n";  // 줄바꿈
        String method = HttpMethod.POST.toString();  // HTTP 메소드
        String accessKey = accessKeyId;  // access key id (from portal or sub account)
        String secretKey = secretAccessKey;  // secret key (from portal or sub account)

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

        HttpHeaders headers = createHttpHeadersForSendingEmail();
        HttpEntity<MailRequest> entity = new HttpEntity<>(mailRequest, headers);

        ResponseEntity<Map<String, Object>> response = restTemplate.exchange(
                domainUrl + emailSendApiUrl, HttpMethod.POST, entity,
                new ParameterizedTypeReference<Map<String, Object>>() {});

        LOGGER.debug("" + response.getBody().toString());
    }

    @Override
    public FileUploadResponse fileUpload(List<Pair<File, String>> fileList) {

        HttpHeaders headers = createHttpHeadersForFileUpload();
        MultiValueMap<String, Object> body = new LinkedMultiValueMap<>();

        for (Pair<File, String> fileInfo : fileList) {
            FileSystemResource fileSystemResource = new FileSystemResource(fileInfo.getLeft()) {
                @Override
                public String getFilename(){
                    return encodeFileName(fileInfo.getRight());
                }
            };

            body.add("fileList", fileSystemResource);
        }

        HttpEntity<MultiValueMap<String, Object>> entity = new HttpEntity<>(body, headers);

        ResponseEntity<FileUploadResponse> response = restTemplate.exchange(
                domainUrl + fileUploadApiUrl, HttpMethod.POST, entity,
                FileUploadResponse.class);

        if (!response.getStatusCode().is2xxSuccessful()) {
            LOGGER.error("file upload error. {}", response);
            throw new RuntimeException("file uplaod error. [" + response.getStatusCode() + "]");
        }

        return response.getBody();
    }

    private HttpHeaders createHttpHeadersForSendingEmail() {
        HttpHeaders headers = new HttpHeaders();
        headers.add(HttpHeaders.CONTENT_TYPE, MediaType.APPLICATION_JSON_VALUE);
        addXNcpHeaderInfo(headers, emailSendApiUrl);

        return headers;
    }

    private HttpHeaders createHttpHeadersForFileUpload() {
        HttpHeaders headers = new HttpHeaders();
        headers.add(HttpHeaders.CONTENT_TYPE, MediaType.MULTIPART_FORM_DATA_VALUE);
        headers.add("accept", MediaType.APPLICATION_JSON_VALUE);
        addXNcpHeaderInfo(headers, fileUploadApiUrl);

        return headers;
    }

    public void addXNcpHeaderInfo(HttpHeaders headers, String apiUrl) {
        String timestamp = String.valueOf(Instant.now().toEpochMilli()); // "1664506837308";  // 현재 타임스탬프 (epoch, millisecond)
        headers.add("x-ncp-apigw-timestamp", timestamp);
        headers.add("x-ncp-iam-access-key", accessKeyId);
        headers.add("x-ncp-apigw-signature-v2", makeSignature(apiUrl, timestamp));
    }

    private String encodeFileName(String originalFileName) {
        try {
            return URLEncoder.encode(originalFileName,StandardCharsets.UTF_8.name())
                    .replaceAll("\\+", "%20");
        } catch (UnsupportedEncodingException e) {
            LOGGER.error("file name encoding error. original file name: {}", originalFileName, e);
            throw new RuntimeException(e);
        }
    }

}
