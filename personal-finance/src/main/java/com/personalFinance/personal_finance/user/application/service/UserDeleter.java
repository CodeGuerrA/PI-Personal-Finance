package com.personalFinance.personal_finance.user.application.service;

import com.personalFinance.personal_finance.user.application.orchestrator.DeleteUserOrchestrator;
import com.personalFinance.personal_finance.user.domain.entity.User;
import com.personalFinance.personal_finance.user.domain.exception.UserNotFoundException;
import com.personalFinance.personal_finance.user.domain.port.UserFindPort;
import jakarta.transaction.Transactional;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

/**
 * Serviço de aplicação para deleção de usuários.
 * Orquestra o fluxo: busca → deleção banco → deleção Keycloak.
 * Segue Dependency Inversion Principle (depende de Ports).
 */
@Slf4j
@Service
@RequiredArgsConstructor
public class UserDeleter {
    private final UserFindPort userFindPort;
    private final DeleteUserOrchestrator deleteUserOrchestrator;

    @Transactional
    public void deleteUser(String keycloakId) {
        log.info("Solicitação de exclusão de usuário com Keycloak ID: {}", keycloakId);

        User user = userFindPort.findByKeycloakId(keycloakId)
                .orElseThrow(() -> {
                    log.warn("Usuário com ID '{}' não encontrado no banco.", keycloakId);
                    return new UserNotFoundException(keycloakId);
                });

        deleteUserOrchestrator.deleteWithConsistency(user, keycloakId);

        log.info("Usuário '{}' deletado com sucesso.", user.getUserName());
    }
}
