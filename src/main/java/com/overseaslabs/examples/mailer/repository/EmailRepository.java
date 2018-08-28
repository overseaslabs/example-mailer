package com.overseaslabs.examples.mailer.repository;

import com.overseaslabs.examples.mailer.entity.Email;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface EmailRepository extends JpaRepository<Email, Integer> {
}