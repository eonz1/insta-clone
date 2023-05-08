package com.cgram.prom.infra.mail.service;

import com.cgram.prom.infra.mail.model.MailRequest;
import com.cgram.prom.infra.mail.model.SmtpMailProperties;
import jakarta.mail.Authenticator;
import jakarta.mail.Message;
import jakarta.mail.MessagingException;
import jakarta.mail.Multipart;
import jakarta.mail.PasswordAuthentication;
import jakarta.mail.Session;
import jakarta.mail.Transport;
import jakarta.mail.internet.InternetAddress;
import jakarta.mail.internet.MimeBodyPart;
import jakarta.mail.internet.MimeMessage;
import jakarta.mail.internet.MimeMultipart;
import java.io.UnsupportedEncodingException;
import java.util.Date;
import java.util.Properties;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Component;

@Slf4j
@RequiredArgsConstructor
@Component
public class SmtpMail implements MailSender {

    private final SmtpMailProperties smtpMailProperties;

    @Override
    public void send(MailRequest mailRequest) {
        final String id = smtpMailProperties.getId();
        final String password = smtpMailProperties.getPwd();
        final String host = smtpMailProperties.getHost();
        final String port = smtpMailProperties.getPort();

        Properties props = new Properties();
        props.put("mail.smtp.host", host);
        props.put("mail.smtp.port", port);
        props.put("mail.smtp.auth", "true");
        props.put("mail.smtp.ssl.trust", host);

        if (port.equals("587")) {
            props.put("mail.smtp.starttls.enable", "true");
        } else if (port.equals("465")) {
            props.put("mail.smtp.ssl.enable", "true");
        }

        try {
            Session session = Session.getInstance(props, new Authenticator() {
                @Override
                protected PasswordAuthentication getPasswordAuthentication() {
                    return new PasswordAuthentication(id, password);
                }
            });

            MimeMessage message = new MimeMessage(session);

            // 날짜
            message.setSentDate(new Date());

            // 발신자 정보
            message.setFrom(
                new InternetAddress(mailRequest.getSenderAddress(), mailRequest.getSenderName()));

            // 수신자 메일주소
            InternetAddress[] recipients = new InternetAddress[mailRequest.getRecipients().size()];
            for (int i = 0; i < mailRequest.getRecipients().size(); i++) {
                recipients[i] = new InternetAddress(
                    mailRequest.getRecipients().get(i).getAddress());
            }
            message.setRecipients(Message.RecipientType.TO, recipients);
            // Message.RecipientType.TO : 받는 사람
            // Message.RecipientType.CC : 참조
            // Message.RecipientType.BCC : 숨은 참조

            // 메일 제목
            message.setSubject(mailRequest.getTitle(), "UTF-8");

            // 메일 내용
            Multipart multipart = new MimeMultipart("alternative");

            MimeBodyPart htmlPart = new MimeBodyPart();
            htmlPart.setHeader("Content-Type", "text/html");
            htmlPart.setContent(mailRequest.getBody(), "text/html; charset=utf-8");

            multipart.addBodyPart(htmlPart);
            message.setContent(multipart);

            // 메일 전송
            Transport.send(message);
        } catch (MessagingException | UnsupportedEncodingException e) {
            log.error("[message: {}, senderAddress: {}, recipientAddress: {}]", e.getMessage(),
                mailRequest.getSenderAddress(), mailRequest.getRecipients().get(0).getAddress());
        }
    }
}
