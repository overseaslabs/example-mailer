package com.overseaslabs.examples.mailer;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.overseaslabs.examples.ureg.entity.User;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.data.redis.connection.Message;
import org.springframework.test.context.junit.jupiter.SpringExtension;

import java.io.IOException;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.*;

@ExtendWith(SpringExtension.class)
@SpringBootTest
class RedisMessageListenerTest {
    @MockBean
    private ObjectMapper om;

    @MockBean
    private Notifier notifier;

    @Autowired
    private RedisMessageListener listener;

    @Configuration
    static class Config {
        @Bean
        public RedisMessageListener getListener() {
            return new RedisMessageListener();
        }
    }

    @Test
    void testOnMessage() throws IOException {
        User u = new User();
        when(om.readValue(any(byte[].class), eq(User.class))).thenReturn(u);

        listener.onMessage(new Message() {
            @Override
            public byte[] getBody() {
                return new byte[0];
            }

            @Override
            public byte[] getChannel() {
                return new byte[0];
            }
        }, new byte[]{1, 2, 3});

        verify(om, times(1)).readValue(any(byte[].class), eq(User.class));
        verify(notifier, times(1)).notify(u);
    }
}
