package com.overseaslabs.examples.mailer;

import java.io.IOException;

/**
 * Email builder
 * Prepares and sends an email
 *
 * @param <T> Mail provider-specific response
 */
interface EmailBuilder<T> {
    EmailBuilder from(String email, String name);

    EmailBuilder to(String email, String name);

    EmailBuilder subject(String subject);

    EmailBuilder content(String content);

    /**
     * Send the built email
     */
    T send() throws IOException;
}