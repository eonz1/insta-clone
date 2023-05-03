package com.cgram.prom.tempmail.ncloud.model;

public class RecipientForRequest {

    private String address = null;
    private String name = null;
    private String type = "R";
    private Object parameters = null;

    public RecipientForRequest() {
    }

    public RecipientForRequest(String address) {
        this.address = address;
    }

    public String getAddress() {
        return address;
    }

    public void setAddress(String address) {
        this.address = address;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getType() {
        return type;
    }

    public void setType(String type) {
        this.type = type;
    }

    public Object getParameters() {
        return parameters;
    }

    public void setParameters(Object parameters) {
        this.parameters = parameters;
    }
}
