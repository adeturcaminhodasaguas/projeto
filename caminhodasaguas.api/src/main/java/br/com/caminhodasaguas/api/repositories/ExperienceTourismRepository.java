package br.com.caminhodasaguas.api.repositories;

import java.util.Optional;
import java.util.UUID;

import org.springframework.data.jpa.repository.JpaRepository;

import br.com.caminhodasaguas.api.domains.ExperienceTourismDomain;

public interface ExperienceTourismRepository extends JpaRepository<ExperienceTourismDomain, UUID> {
    Optional<ExperienceTourismDomain> findByUrl(String url);
}
