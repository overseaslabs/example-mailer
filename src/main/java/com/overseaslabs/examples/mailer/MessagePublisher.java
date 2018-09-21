package com.overseaslabs.examples.mailer;

interface MessagePublisher {

    /**
     * Publish a message
     * @param message The message to publish
     */
    void publish(Object message);
}