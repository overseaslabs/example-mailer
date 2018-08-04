package com.overseaslabs.examples.mailer.controller;

import com.overseaslabs.examples.mailer.entity.Email;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.RestController;

import javax.servlet.http.HttpServletRequest;
import java.util.ArrayList;
import java.util.List;

/**
 * Exposes the microservice's API
 */
@RestController
public class ApiController {

    /**
     * Find the email
     *
     * @param id The ID of the user
     * @return Email instance
     */
    @GetMapping("/emails/{id}")
    public Email get(@PathVariable int id) {
        return new Email();
    }

    /**
     * Find the emails matching to the search request
     *
     * @param request HTTP request
     * @return The found emails
     */
    @GetMapping("/emails")
    public List<Email> find(HttpServletRequest request) {
        return new ArrayList<>();
    }
}
