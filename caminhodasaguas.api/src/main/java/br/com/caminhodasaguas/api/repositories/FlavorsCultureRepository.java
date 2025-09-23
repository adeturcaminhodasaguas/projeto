package br.com.caminhodasaguas.api.repositories;

import java.util.Optional;
import java.util.UUID;

import org.springframework.data.jpa.repository.JpaRepository;

import br.com.caminhodasaguas.api.domains.FlavorsCultureDomain;

public interface FlavorsCultureRepository extends JpaRepository<FlavorsCultureDomain, UUID> {
    Optional<FlavorsCultureDomain> findByUrl(String url);
}
