package com.cgram.prom.tempmail.ncloud.model;

import java.util.ArrayList;
import java.util.List;

public class MailRequest {

    private String senderAddress;
    private String senderName;
    private String title;
    private String body;
    private Boolean individual = true;

    private List<String> attachFileIds;

    private List<RecipientForRequest> recipients;

    private Boolean advertising = false;

    public String getSenderAddress() {
        return senderAddress;
    }

    public void setSenderAddress(String senderAddress) {
        this.senderAddress = senderAddress;
    }

    public String getSenderName() {
        return senderName;
    }

    public void setSenderName(String senderName) {
        this.senderName = senderName;
    }

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public String getBody() {
        return body;
    }

    public void setBody(String body) {
        this.body = body;
    }

    public Boolean getIndividual() {
        return individual;
    }

    public void setIndividual(Boolean individual) {
        this.individual = individual;
    }

    public List<String> getAttachFileIds() {
        return attachFileIds;
    }

    public void setAttachFileIds(List<String> attachFileIds) {
        this.attachFileIds = attachFileIds;
    }

    public List<RecipientForRequest> getRecipients() {
        return recipients;
    }

    public void setRecipients(List<RecipientForRequest> recipients) {
        this.recipients = recipients;
    }

    public void addRecipient(String address) {
        if (this.recipients == null) {
            this.recipients = new ArrayList<>();
        }

        recipients.add(new RecipientForRequest(address));
    }

    public void addRecipient(RecipientForRequest recipient) {
        if (this.recipients == null) {
            this.recipients = new ArrayList<>();
        }

        recipients.add(recipient);
    }

    public Boolean getAdvertising() {
        return advertising;
    }

    public void setAdvertising(Boolean advertising) {
        this.advertising = advertising;
    }
}
