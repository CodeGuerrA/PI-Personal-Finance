package com.personalFinance.personal_finance.user.application.orchestrator;

import com.personalFinance.personal_finance.user.domain.entity.User;
import com.personalFinance.personal_finance.user.domain.exception.UserPersistenceException;
import com.personalFinance.personal_finance.user.domain.port.UserSavePort;
import com.personalFinance.personal_finance.user.infrastructure.external.keycloak.facade.KeycloakFacade;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.dao.DataIntegrityViolationException;
import org.springframework.stereotype.Service;

/**
 * Orquestra a operação de salvar usuário com rollback automático no Keycloak em caso de falha.
 * Coordena a consistência transacional entre banco de dados e Keycloak.
 * <p>
 * Agora trabalha apenas com entidades de domínio (não mais DTOs).
 * Segue Clean Architecture: Application layer não deve conhecer detalhes da API layer.
 */
@Slf4j
@Service
@RequiredArgsConstructor
public class SaveUserOrchestrator {

    private final UserSavePort userSavePort;
    private final RollbackStrategy rollbackStrategy;

    /**
     * Salva usuário com rollback automático no Keycloak em caso de falha.
     *
     * @param user       Entidade de domínio já validada
     * @param keycloakId ID do usuário no Keycloak
     * @return Usuário salvo com ID gerado
     */
    public User saveWithRollback(User user, String keycloakId) {
        try {
            User savedUser = userSavePort.save(user);

            log.info("Usuário '{}' criado com sucesso no banco de dados com ID: {}",
                    user.getUserName(), savedUser.getId());
            return savedUser;

        } catch (DataIntegrityViolationException e) {
            log.error("Erro ao salvar usuário '{}' no banco. Iniciando rollback no Keycloak.",
                    user.getUserName(), e);
            rollbackStrategy.performRollback(keycloakId, user.getUserName());

            throw new UserPersistenceException(
                    "Falha ao salvar usuário no banco. Usuário removido do Keycloak: " + user.getEmail(), e);

        } catch (Exception e) {
            log.error("Erro inesperado ao salvar usuário '{}' no banco. Iniciando rollback no Keycloak.",
                    user.getUserName(), e);
            rollbackStrategy.performRollback(keycloakId, user.getUserName());

            throw new UserPersistenceException("Erro inesperado ao criar usuário: " + e.getMessage(), e);
        }
    }
}
