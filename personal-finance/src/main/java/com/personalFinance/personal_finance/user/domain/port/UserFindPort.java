package com.personalFinance.personal_finance.user.domain.port;

import com.personalFinance.personal_finance.user.domain.entity.User;
import org.springframework.data.repository.query.Param;

import java.util.List;
import java.util.Optional;

/**
 * Port para operações de consulta de usuários.
 * Responsável apenas por buscas - métodos de existência estão em UserExistencePort.
 */
public interface UserFindPort {
    List<User> findAll();

    Optional<User> findByKeycloakId(@Param("keycloakId") String keycloakId);

    Optional<User> findByEmail(@Param("email") String email);

    Optional<String>findKeycloakIdByLocalId(@Param("localId") Long localId);
}
