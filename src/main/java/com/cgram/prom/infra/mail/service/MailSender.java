package com.cgram.prom.infra.mail.service;

import com.cgram.prom.infra.mail.model.MailRequest;

public interface MailSender {

    void send(MailRequest mailRequest);
}
