package com.swizzyindustries.auth_service.models;

import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;

public interface CodeLordRepository extends JpaRepository<CodeLord, Long> {
    Optional<CodeLord> findByEmail(String email);
}
