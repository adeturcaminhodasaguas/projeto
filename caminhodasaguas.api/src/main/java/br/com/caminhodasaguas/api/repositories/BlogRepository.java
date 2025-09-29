package br.com.caminhodasaguas.api.repositories;

import java.util.Optional;
import java.util.UUID;

import org.springframework.data.jpa.repository.JpaRepository;

import br.com.caminhodasaguas.api.domains.BlogDomain;

public interface BlogRepository extends JpaRepository<BlogDomain, UUID> {
    Optional<BlogDomain> findByUrl(String url);
}
