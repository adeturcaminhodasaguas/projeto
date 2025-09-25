package br.com.caminhodasaguas.api.repositories;

import java.util.Optional;
import java.util.UUID;

import org.springframework.data.jpa.repository.JpaRepository;

import br.com.caminhodasaguas.api.domains.EventDomain;

public interface EventRepository extends JpaRepository<EventDomain, UUID> {
    Optional<EventDomain> findByUrl(String url);
}