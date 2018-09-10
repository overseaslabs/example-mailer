package com.overseaslabs.examples.mailer.controller;

import com.overseaslabs.examples.mailer.entity.Email;
import com.overseaslabs.examples.mailer.repository.EmailRepository;
import com.overseaslabs.examples.utils.exception.ResourceNotFoundException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RestController;

import java.util.Optional;

/**
 * Exposes the microservice's API
 */
@RestController
public class ApiController {

    @Autowired
    private EmailRepository emailRepository;

    /**
     * Find the email
     *
     * @param id The ID of the user
     * @return Email instance
     */
    @GetMapping("/emails/{id}")
    public Email get(@PathVariable int id) throws ResourceNotFoundException {
        return emailRepository.findById(id).orElseThrow(() -> new ResourceNotFoundException("Email " + id + " not found"));
    }

    /**
     * Find the emails matching to the search request
     *
     * @param pageable Pagination info
     * @return The found emails
     */
    @GetMapping("/emails")
    public Page<Email> find(Pageable pageable) {
        return emailRepository.findAll(pageable);
    }
}
