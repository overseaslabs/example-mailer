package com.overseaslabs.examples.mailer.entity;

/**
 * Mail service provider response
 */
public class ProviderResponse {
    private Email email;
    private Boolean success;
    private String message;

    public Email getEmail() {
        return email;
    }

    public ProviderResponse setEmail(Email email) {
        this.email = email;
        return this;
    }

    public Boolean getSuccess() {
        return success;
    }

    public ProviderResponse setSuccess(Boolean success) {
        this.success = success;
        return this;
    }

    public String getMessage() {
        return message;
    }

    public ProviderResponse setMessage(String message) {
        this.message = message;
        return this;
    }
}
