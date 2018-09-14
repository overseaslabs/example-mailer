package com.overseaslabs.examples.mailer;

import com.overseaslabs.examples.mailer.entity.Email;
import com.overseaslabs.examples.mailer.entity.ProviderResponse;
import com.overseaslabs.examples.mailer.repository.EmailRepository;
import com.overseaslabs.examples.ureg.entity.User;
import com.sendgrid.Response;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.io.IOException;

/**
 * Notification service
 * Sends an email to a new user and reports the sending to a redis channel
 */
@Service
public class Notifier {
    private static final String fromEmail = "example@overseaslabs.com";
    private static final String fromName = "Overseas Labs Ltd. - Example Project";

    @Autowired
    private EmailRepository emailRepository;

    @Autowired
    private RedisMessagePublisher publisher;

    @Autowired
    private EmailBuilder<Response> sendgridEmailBuilder;

    /**
     * Notify a user
     *
     * @param user The user to notify
     */
    void notify(User user) {
        String fullName = user.getFirstName() + " " + user.getLastName();
        String content = String.format("Hi, %s. This email has been sent by the example project. Have a nice day!", fullName);

        ProviderResponse providerResponse = null;

        try {
            sendgridEmailBuilder.from(fromEmail, fromName)
                    .to(user.getEmail(), fullName)
                    .subject(String.format("Hi, %s!", fullName))
                    .content(content)
                    .send();

            Email email = new Email();

            email.setRecipient(fullName)
                    .setEmail(user.getEmail())
                    .setContent(content);

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
