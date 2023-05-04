package com.cgram.prom.infra.mail;

import static org.junit.jupiter.api.Assertions.assertEquals;

import com.cgram.prom.infra.mail.model.MailRequest;
import com.cgram.prom.infra.mail.model.SmtpMailProperties;
import com.cgram.prom.infra.mail.service.MailSender;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.context.properties.EnableConfigurationProperties;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.ActiveProfiles;

@DisplayName("메일 발송 기능테스트")
@SpringBootTest
@ActiveProfiles(profiles = "dev")
@EnableConfigurationProperties(value = SmtpMailProperties.class)
class sendMailTest {

    @Autowired
    private SmtpMailProperties smtpMailProperties;

    @Autowired
    private MailSender ncloudMail;

    @Autowired
    private MailSender smtpMail;

    @Test
    void 메일_환경설정_계정_가져오기() {
        assertEquals("smtp.gmail.com", smtpMailProperties.getHost());
        assertEquals("587", smtpMailProperties.getPort());
        assertEquals("meonzzz1@gmail.com", smtpMailProperties.getId());
    }

    @Test
    void 네이버클라우드_메일_보내기() {
        MailRequest request = new MailRequest();
        request.setSenderAddress("test@test.com");
        request.setSenderName("ncloud 메일전송 테스트 발송인");
        request.setTitle("테스트 메일 전송 타이틀");
        request.setBody("메일 내용<br><br>html 태그 확인<hr>");
        request.addRecipient("meonzzz1@gmail.com");

        ncloudMail.send(request);
    }

    @Test
    void 구글_메일_보내기() {
        MailRequest request = new MailRequest();
        request.setSenderAddress("test@test.com");
        request.setSenderName("구글 메일전송 테스트 발송인");
        request.setTitle("테스트 메일 전송 타이틀");
        request.setBody("메일 내용<br><br>html 태그 확인<hr>");
        request.addRecipient("meonzzz1@gmail.com");

        smtpMail.send(request);
    }
}