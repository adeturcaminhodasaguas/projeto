package br.com.caminhodasaguas.api.repositories;

import java.util.UUID;

import org.springframework.data.jpa.repository.JpaRepository;

import br.com.caminhodasaguas.api.domains.BannerDomain;

public interface BannerRepository extends JpaRepository<BannerDomain, UUID> {}