package com.personalFinance.personal_finance.user.application.orchestrator;

import com.personalFinance.personal_finance.user.infrastructure.external.keycloak.facade.KeycloakFacade;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Component;

/**
 * Implementação de rollback específica para Keycloak.
 * Segue Strategy Pattern e Single Responsibility Principle.
 */
@Slf4j
@Component
@RequiredArgsConstructor
public class KeycloakRollbackStrategy implements RollbackStrategy {

    private final KeycloakFacade keycloakFacade;

    @Override
    public void performRollback(String keycloakId, String username) {
        try {
            keycloakFacade.deleteUser(keycloakId);
            log.info("Rollback executado: usuário '{}' removido do Keycloak.", username);
        } catch (Exception rollbackException) {
            log.error("ERRO CRÍTICO: Falha no rollback do Keycloak para usuário '{}'. ID: {}",
                    username, keycloakId, rollbackException);
        }
    }
}
