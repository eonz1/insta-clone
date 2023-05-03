package com.cgram.prom.infra.mail;

import java.util.List;

public interface MailSender {

    void send(String fromName, List<String> receives, String title, String body);
}
