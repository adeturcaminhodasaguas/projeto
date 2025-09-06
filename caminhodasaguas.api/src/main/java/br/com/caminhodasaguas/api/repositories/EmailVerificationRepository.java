package br.com.caminhodasaguas.api.repositories;

import org.springframework.data.jpa.repository.JpaRepository;

import br.com.caminhodasaguas.api.domains.EmailVerificationDomain;

import java.util.Optional;
import java.util.UUID;

public interface EmailVerificationRepository extends JpaRepository<EmailVerificationDomain, UUID> {
    Optional<EmailVerificationDomain> findByEmail(String email);
}