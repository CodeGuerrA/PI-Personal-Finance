package com.personalFinance.personal_finance.user.infrastructure.repository;

import com.personalFinance.personal_finance.user.domain.entity.User;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

/**
 * Repository JPA para User.
 * Utiliza JPQL explícito para todas as queries.
 */
@Repository
public interface UserRepository extends JpaRepository<User, Long> {

    /**
     * Verifica se existe um usuário com o email fornecido.
     */
    @Query("SELECT CASE WHEN COUNT(u) > 0 THEN true ELSE false END FROM User u WHERE u.email = :email")
    boolean existsByEmail(@Param("email") String email);

    /**
     * Verifica se existe um usuário com o username fornecido.
     */
    @Query("SELECT CASE WHEN COUNT(u) > 0 THEN true ELSE false END FROM User u WHERE u.userName = :userName")
    boolean existsByUserName(@Param("userName") String userName);

    /**
     * Verifica se existe um usuário com o CPF fornecido.
     */
    @Query("SELECT CASE WHEN COUNT(u) > 0 THEN true ELSE false END FROM User u WHERE u.cpf = :cpf")
    boolean existsByCpf(@Param("cpf") String cpf);

    /**
     * Busca um usuário pelo Keycloak ID.
     */
    @Query("SELECT u FROM User u WHERE u.keycloakId = :keycloakId")
    Optional<User> findByKeycloakId(@Param("keycloakId") String keycloakId);

    /**
     * Busca o Keycloak ID de um usuário pelo ID local.
     */
    @Query("SELECT u.keycloakId FROM User u WHERE u.id = :localId")
    Optional<String> findKeycloakIdByLocalId(@Param("localId") Long localId);

    /**
     * Busca um usuário pelo email.
     */
    @Query("SELECT u FROM User u WHERE u.email = :email")
    Optional<User> findByEmail(@Param("email") String email);

    /**
     * Busca um usuário pelo username.
     */
    @Query("SELECT u FROM User u WHERE u.userName = :userName")
    Optional<User> findByUserName(@Param("userName") String userName);

    /**
     * Busca usuários por nome (busca parcial case-insensitive).
     */
    @Query("SELECT u FROM User u WHERE LOWER(u.firstName) LIKE LOWER(CONCAT('%', :nome, '%')) OR LOWER(u.lastName) LIKE LOWER(CONCAT('%', :nome, '%'))")
    List<User> findByNomeContaining(@Param("nome") String nome);

    /**
     * Conta total de usuários cadastrados.
     */
    @Query("SELECT COUNT(u) FROM User u")
    Long countAllUsers();

    /**
     * Busca usuários ordenados por ID (mais recentes primeiro assumindo IDs crescentes).
     */
    @Query("SELECT u FROM User u ORDER BY u.id DESC")
    List<User> findAllOrderByIdDesc();
}
