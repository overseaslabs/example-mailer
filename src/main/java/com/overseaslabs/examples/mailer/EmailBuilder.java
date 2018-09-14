package com.overseaslabs.examples.mailer;

import java.io.IOException;

/**
 * Email builder
 * Prepares and sends an email
 *
 * @param <T> Mail provider-specific response type
 */
interface EmailBuilder<T> {
    EmailBuilder from(String email, String name);

    EmailBuilder to(String email, String name);

    EmailBuilder subject(String subject);

    EmailBuilder content(String content);

    /**
     * Send the built email
     * @return mail provider response
     * @throws IOException mail provider-specific exception
     */
    T send() throws IOException;
}