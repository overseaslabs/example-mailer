package com.overseaslabs.examples.mailer;

import com.overseaslabs.examples.mailer.entity.ProviderResponse;
import com.overseaslabs.examples.mailer.repository.EmailRepository;
import com.overseaslabs.examples.ureg.entity.User;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
import com.sendgrid.*;

import java.io.IOException;

/**
 * Notification service
 */
@Service
public class Notifier {

    private static final String fromEmail = "example@overseaslabs.com";
    private static final String fromName = "Overseas Labs Ltd. - Example Project";

    @Value("${SENDGRID_API_KEY}")
    private String apiKey;

    @Autowired
    private EmailRepository emailRepository;

    @Autowired
    private RedisMessagePublisher publisher;

    /**
     * Notify a user
     *
     * @param user The user to notify
     */
    void notify(User user) {
        String fullName = user.getFirstName() + " " + user.getLastName();

        Email from = new Email(fromEmail, fromName);
        String subject = String.format("Hi, %s!", fullName);
        Email to = new Email(user.getEmail(), fullName);
        Content content = new Content("text/plain", String.format("Hi, %s. This email has been sent by the example project. Have a nice day!", fullName));
        Mail mail = new Mail(from, subject, to, content);

        SendGrid sg = new SendGrid(apiKey);
        Request request = new Request();

        ProviderResponse providerResponse = null;

        try {
            request.setMethod(Method.POST);
            request.setEndpoint("mail/send");
            request.setBody(mail.build());
            sg.api(request);

            com.overseaslabs.examples.mailer.entity.Email email = new com.overseaslabs.examples.mailer.entity.Email();

            email.setRecipient(fullName)
                    .setEmail(user.getEmail())
                    .setContent(content.getValue());

            emailRepository.save(email);

            providerResponse = new ProviderResponse(true, "An email has been successfully sent (reported via a websocket)");


        } catch (IOException ex) {
            providerResponse = new ProviderResponse(false, ex.getMessage() + " (reported via a websocket)");
            ex.printStackTrace();

        } finally {
            //send the response to the web server
            publisher.publish(providerResponse);
        }
    }
}
