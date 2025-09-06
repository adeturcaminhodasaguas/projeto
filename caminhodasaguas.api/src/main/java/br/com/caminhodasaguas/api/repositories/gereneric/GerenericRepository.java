package br.com.caminhodasaguas.api.repositories.gereneric;

import java.util.List;
import java.util.Optional;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.repository.NoRepositoryBean;

@NoRepositoryBean
public interface GerenericRepository<T, ID> extends JpaRepository<T, ID> {
    List<T> findAllByDeletedAtIsNull();
    Optional<T> findByIdAndDeletedAtIsNull(ID id);
}