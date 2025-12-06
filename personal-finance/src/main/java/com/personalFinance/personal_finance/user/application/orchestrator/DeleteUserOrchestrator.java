package com.personalFinance.personal_finance.user.application.orchestrator;

import com.personalFinance.personal_finance.user.domain.entity.User;
import com.personalFinance.personal_finance.user.domain.port.UserDeletePort;
import com.personalFinance.personal_finance.user.infrastructure.external.keycloak.facade.KeycloakFacade;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

/**
 * Orquestra a operação de deletar usuário mantendo consistência entre banco e Keycloak.
 * Coordena a remoção em ambos os sistemas de forma transacional.
 */
@Slf4j
@Service
@RequiredArgsConstructor
public class DeleteUserOrchestrator {

    private final UserDeletePort userDeletePort;
    private final KeycloakFacade keycloakFacade;

    /**
     * Deleta usuário com consistência entre banco e Keycloak.
     *
     * @param user Entidade a ser deletada
     * @param keycloakId ID do usuário no Keycloak
     */
    public void deleteWithConsistency(User user, String keycloakId) {
        // Deleta do banco
        userDeletePort.delete(user.getId());
        log.info("Usuário '{}' removido do banco de dados local.", user.getUserName());

        // Deleta do Keycloak
        keycloakFacade.deleteUser(keycloakId);
        log.info("Usuário '{}' removido do Keycloak com sucesso.", user.getUserName());
    }
}
