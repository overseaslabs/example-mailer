package com.overseaslabs.examples.mailer;

import org.springframework.data.redis.connection.Message;
import org.springframework.data.redis.connection.MessageListener;

public class RedisMessageListener implements MessageListener {
    public void onMessage(Message message, byte[] pattern) {
        System.out.println("Message received: " + new String(message.getBody()));
    }
}
