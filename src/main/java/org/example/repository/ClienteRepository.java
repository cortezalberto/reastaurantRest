package org.example.repository;

import org.example.entidades.Cliente;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

/**
 * Repositorio Spring Data JPA para la entidad Cliente
 *
 * Implementa operaciones CRUD y consultas personalizadas para clientes.
 * Cumple con las historias de usuario HU-021, HU-023, HU-024, HU-025.
 */
@Repository
public interface ClienteRepository extends JpaRepository<Cliente, Long> {

    /**
     * HU-023: Consultar clientes activos (no eliminados)
     */
    List<Cliente> findByEliminadoFalse();

    /**
     * HU-025: Buscar cliente por email exacto
     */
    Optional<Cliente> findByEmailAndEliminadoFalse(String email);

    /**
     * HU-025: Buscar clientes por nombre (contiene texto)
     */
    List<Cliente> findByNombreContainingIgnoreCaseAndEliminadoFalse(String nombre);

    /**
     * HU-025: Buscar clientes por apellido (contiene texto)
     */
    List<Cliente> findByApellidoContainingIgnoreCaseAndEliminadoFalse(String apellido);

    /**
     * HU-025: Buscar cliente por teléfono exacto
     */
    Optional<Cliente> findByTelefonoAndEliminadoFalse(String telefono);

    /**
     * HU-025: Buscar clientes por nombre y apellido completos
     */
    List<Cliente> findByNombreIgnoreCaseAndApellidoIgnoreCaseAndEliminadoFalse(String nombre, String apellido);

    /**
     * Consulta personalizada: Obtener clientes con información completa
     */
    @Query("SELECT c FROM Cliente c " +
           "LEFT JOIN FETCH c.usuario " +
           "LEFT JOIN FETCH c.domicilios d " +
           "LEFT JOIN FETCH d.localidad l " +
           "LEFT JOIN FETCH l.provincia p " +
           "LEFT JOIN FETCH p.pais " +
           "WHERE c.eliminado = false")
    List<Cliente> findAllActiveWithCompleteInfo();

    /**
     * Consulta personalizada: Verificar si existe email (para validaciones)
     */
    @Query("SELECT COUNT(c) > 0 FROM Cliente c WHERE c.email = :email AND c.eliminado = false AND (:id IS NULL OR c.id != :id)")
    boolean existsByEmailAndNotId(@Param("email") String email, @Param("id") Long id);

    /**
     * HU-023: Obtener cliente con pedidos
     */
    @Query("SELECT c FROM Cliente c LEFT JOIN FETCH c.pedidos WHERE c.id = :id AND c.eliminado = false")
    Optional<Cliente> findByIdWithPedidos(@Param("id") Long id);
}