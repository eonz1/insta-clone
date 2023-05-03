package com.cgram.prom.infra.mail;

import jakarta.mail.Authenticator;
import jakarta.mail.Message;
import jakarta.mail.MessagingException;
import jakarta.mail.Multipart;
import jakarta.mail.PasswordAuthentication;
import jakarta.mail.Session;
import jakarta.mail.Transport;
import jakarta.mail.internet.AddressException;
import jakarta.mail.internet.InternetAddress;
import jakarta.mail.internet.MimeBodyPart;
import jakarta.mail.internet.MimeMessage;
import jakarta.mail.internet.MimeMultipart;
import java.io.UnsupportedEncodingException;
import java.util.Date;
import java.util.List;
import java.util.Properties;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;

@RequiredArgsConstructor
@Component
public class SmtpMail implements MailSender {

    private final MailProperties mailProperties;

    @Override
    public void send(String fromName, List<String> recipientList, String title, String body) {
        final String id = mailProperties.getId();
        final String password = mailProperties.getPwd();
        final String host = mailProperties.getHost();
        final String port = mailProperties.getPort();

        Properties props = new Properties();
        props.put("mail.smtp.host", host);
        props.put("mail.smtp.port", port);
        props.put("mail.smtp.auth", "true");
        props.put("mail.smtp.ssl.trust", host);

        if(port.equals("587")) {
            props.put("mail.smtp.starttls.enable", "true");
        } else if(port.equals("465")) {
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
            message.setFrom(new InternetAddress(id, fromName));

            // 수신자 메일주소
            InternetAddress[] recipients = new InternetAddress[recipientList.size()];
            for(int i = 0; i < recipientList.size(); i++) {
                recipients[i] = new InternetAddress(recipientList.get(i));
            }
            message.setRecipients(Message.RecipientType.TO, recipients);
            // Message.RecipientType.TO : 받는 사람
            // Message.RecipientType.CC : 참조
            // Message.RecipientType.BCC : 숨은 참조

            // 메일 제목
            message.setSubject(title, "UTF-8");

            // 메일 내용
            Multipart multipart = new MimeMultipart( "alternative");

            MimeBodyPart htmlPart = new MimeBodyPart();
            htmlPart.setHeader("Content-Type", "text/html");
            htmlPart.setContent(body, "text/html; charset=utf-8" );

            multipart.addBodyPart(htmlPart);
            message.setContent(multipart);

            // 메일 전송
            Transport.send(message);
        } catch (AddressException e) {
            e.printStackTrace();
        } catch (MessagingException e) {
            e.printStackTrace();
        } catch (UnsupportedEncodingException e) {
            e.printStackTrace();
        }
    }
}
