package org.example.repository;

import org.example.entidades.Usuario;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

/**
 * Repositorio Spring Data JPA para la entidad Usuario
 */
@Repository
public interface UsuarioRepository extends JpaRepository<Usuario, Long> {

    List<Usuario> findByEliminadoFalse();

    Optional<Usuario> findByUsernameAndEliminadoFalse(String username);

    Optional<Usuario> findByAuth0IdAndEliminadoFalse(String auth0Id);
}