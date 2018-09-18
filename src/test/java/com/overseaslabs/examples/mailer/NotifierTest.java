package com.overseaslabs.examples.mailer;

import com.overseaslabs.examples.mailer.entity.Email;
import com.overseaslabs.examples.mailer.entity.ProviderResponse;
import com.overseaslabs.examples.mailer.repository.EmailRepository;
import com.overseaslabs.examples.ureg.entity.User;
import com.sendgrid.Response;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.test.context.junit.jupiter.SpringExtension;

import java.io.IOException;

import static org.mockito.ArgumentMatchers.*;
import static org.mockito.Mockito.*;

@ExtendWith(SpringExtension.class)
@SpringBootTest
class NotifierTest {
    @MockBean
    private EmailRepository repository;

    @MockBean
    private EmailBuilder<Response> emailBuilder;

    @MockBean
    private RedisMessagePublisher publisher;

    @Autowired
    private Notifier notifier;

    @Configuration
    static class Config {
        @Bean
        public Notifier getNotifier() {
            return new Notifier();
        }
    }

    @Test
    void testNotify() throws IOException {
        when(emailBuilder.from(anyString(), anyString())).thenReturn(emailBuilder);
        when(emailBuilder.to(anyString(), anyString())).thenReturn(emailBuilder);
        when(emailBuilder.content(anyString())).thenReturn(emailBuilder);
        when(emailBuilder.subject(anyString())).thenReturn(emailBuilder);

        User u = new User();
        u.setEmail("foo@bar.com").setFirstName("Foo").setLastName("Bar");
        notifier.notify(u);

        verify(emailBuilder, times(1)).send();
        verify(repository, times(1)).save(any(Email.class));
        verify(publisher, times(1)).publish(any(ProviderResponse.class));
    }
}
