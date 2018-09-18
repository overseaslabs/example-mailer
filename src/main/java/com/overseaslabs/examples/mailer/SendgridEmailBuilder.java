package com.overseaslabs.examples.mailer;

import com.sendgrid.*;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Scope;
import org.springframework.stereotype.Component;

import java.io.IOException;

@Component
@Scope("prototype")
class SendgridEmailBuilder implements EmailBuilder<Response> {

    private Email from;
    private Email to;
    private String subject;
    private Content content;

    @Autowired
    private SendGrid sg;

    @Override
    public EmailBuilder from(String email, String name) {
        from = new Email(email, name);
        return this;
    }

    @Override
    public EmailBuilder to(String email, String name) {
        to = new Email(email, name);
        return this;
    }

    @Override
    public EmailBuilder subject(String subject) {
        this.subject = subject;
        return this;
    }

    @Override
    public EmailBuilder content(String content) {
        this.content = new Content("text/plain", content);
        return this;
    }

    @Override
    public Response send() throws IOException {
        Request request = new Request();
        request.setMethod(Method.POST);
        request.setEndpoint("mail/send");
        Mail mail = new Mail(from, subject, to, content);
        request.setBody(mail.build());
        return sg.api(request);
    }
}
