package com.cgram.prom.global.letter;

import static org.junit.jupiter.api.Assertions.*;

import com.cgram.prom.infra.mail.MailProperties;
import com.cgram.prom.infra.mail.MailSender;
import java.util.ArrayList;
import java.util.List;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.context.properties.EnableConfigurationProperties;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.ActiveProfiles;

@DisplayName("메일 발송 기능테스트")
@SpringBootTest
@ActiveProfiles(profiles = "dev")
@EnableConfigurationProperties(value = MailProperties.class)
class sendMailTest {

    @Autowired
    private MailProperties mailProperties;

    @Autowired
    private MailSender mailSender;

    @Test
    void 메일_환경설정_계정_가져오기() {
        assertEquals("smtp.gmail.com", mailProperties.getHost());
        assertEquals("587", mailProperties.getPort());
        assertEquals("meonzzz1@gmail.com", mailProperties.getId());
    }

    @Test
    void 포트465_SSL_메일_발송() {
        List<String> list = new ArrayList<>();
        list.add("meonzzz1@gmail.com");

        mailProperties.setPort("465");

        mailSender.send("씨그램", list, "제목", "내용");
    }
    @Test
    void 포트587_TLS_메일_발송() {
        List<String> list = new ArrayList<>();
        list.add("meonzzz1@gmail.com");

        mailProperties.setPort("587");

        mailSender.send("씨그램", list, "제목", "내용");
    }
}