package com.overseaslabs.examples.mailer;

import com.overseaslabs.examples.mailer.controller.ApiController;
import com.overseaslabs.examples.mailer.repository.EmailRepository;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.Pageable;
import org.springframework.data.web.config.EnableSpringDataWebSupport;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.test.context.junit.jupiter.SpringExtension;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.MvcResult;
import org.springframework.test.web.servlet.request.MockMvcRequestBuilders;

import static org.mockito.Mockito.*;

import com.overseaslabs.examples.mailer.entity.Email;

import java.util.Collections;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;

@ExtendWith(SpringExtension.class)
@WebMvcTest(ApiController.class)
@EnableSpringDataWebSupport
class ApiControllerTest {
    @Autowired
    MockMvc mvc;

    @MockBean
    EmailRepository repository;

    @BeforeEach
    void beforeEach() {
        when(repository.findById(any(Integer.class))).thenReturn(Optional.of(new Email()));
        Page<Email> emailPage =  new PageImpl<>(Collections.singletonList(new Email()));
        when(repository.findAll(any(Pageable.class))).thenReturn(emailPage);
    }

    @Test
    void testGetEmail() throws Exception {
        MvcResult result = mvc.perform(MockMvcRequestBuilders.get("/emails/1").contentType(MediaType.APPLICATION_JSON)).andReturn();
        assertEquals(HttpStatus.OK.value(), result.getResponse().getStatus());
    }

    @Test
    void testFindEmails() throws Exception {
        MvcResult result = mvc.perform(MockMvcRequestBuilders.get("/emails").contentType(MediaType.APPLICATION_JSON)).andReturn();
        assertEquals(HttpStatus.OK.value(), result.getResponse().getStatus());
    }
}
