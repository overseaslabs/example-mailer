package com.overseaslabs.examples.mailer;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.overseaslabs.examples.ureg.entity.User;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.redis.connection.Message;
import org.springframework.data.redis.connection.MessageListener;

import java.io.IOException;

public class RedisMessageListener implements MessageListener {

    @Autowired
    ObjectMapper om;

    @Autowired
    Notifier notifier;

    public void onMessage(Message message, byte[] pattern) {
        User user = null;

        try {
            user = om.readValue(message.getBody(), User.class);
        } catch (IOException e) {
            e.printStackTrace();
        }

        if (user != null) {
            notifier.notify(user);
        }
    }
}
