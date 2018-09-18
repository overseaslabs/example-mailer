package com.overseaslabs.examples.mailer;

import com.sendgrid.Request;
import com.sendgrid.Response;
import com.sendgrid.SendGrid;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.test.context.junit.jupiter.SpringExtension;

import java.io.IOException;

import static org.mockito.Mockito.*;

@ExtendWith(SpringExtension.class)
@SpringBootTest
class SendgridEmailBuilderTest {
    @Autowired
    private SendgridEmailBuilder builder;

    @MockBean
    private SendGrid sg;

    @Configuration
    static class Config {
        @Bean
        SendgridEmailBuilder builder() {
            return new SendgridEmailBuilder();
        }
    }

    @Test
    void testBuilder() throws IOException {
        when(sg.api(any(Request.class))).thenReturn(any(Response.class));

        builder.from("foo", "bar")
                .to("foo", "bar")
                .subject("foo")
                .content("foo")
                .send();
    }
}
