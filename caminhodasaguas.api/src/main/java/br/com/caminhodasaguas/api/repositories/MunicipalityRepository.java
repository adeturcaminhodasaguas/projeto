package br.com.caminhodasaguas.api.repositories;

import br.com.caminhodasaguas.api.domains.MunicipalityDomain;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;
import java.util.UUID;

public interface MunicipalityRepository extends JpaRepository<MunicipalityDomain, UUID> {
    Optional<MunicipalityDomain> findByUrl(String url);
}