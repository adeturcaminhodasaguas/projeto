package br.com.caminhodasaguas.api.repositories;

import java.util.Optional;
import java.util.UUID;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import br.com.caminhodasaguas.api.domains.UserDomain;

@Repository
public interface UserRepository extends JpaRepository<UserDomain, UUID> {
    Optional<UserDomain> findByEmailAndDeletedAtIsNull(String email);
    Optional<UserDomain> findByDocumentAndDeletedAtIsNull(String document);
    Optional<UserDomain> findByPhoneAndDeletedAtIsNull(String phone);
}