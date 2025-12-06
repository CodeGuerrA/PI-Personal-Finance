package com.personalFinance.personal_finance.user.application.service;

import com.personalFinance.personal_finance.shared.validator.ValidationResult;
import com.personalFinance.personal_finance.user.api.dto.request.UserUpdateRequestDTO;
import com.personalFinance.personal_finance.user.application.orchestrator.UpdateUserOrchestrator;
import com.personalFinance.personal_finance.user.domain.entity.User;
import com.personalFinance.personal_finance.user.domain.exception.DuplicateEmailException;
import com.personalFinance.personal_finance.user.domain.exception.UserNotFoundException;
import com.personalFinance.personal_finance.user.domain.port.UserFindPort;
import com.personalFinance.personal_finance.user.domain.service.validation.EmailNormalizer;
import com.personalFinance.personal_finance.user.domain.service.validation.EmailUpdateValidator;
import com.personalFinance.personal_finance.user.infrastructure.external.keycloak.facade.KeycloakFacade;
import jakarta.transaction.Transactional;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

/**
 * Serviço de aplicação para atualização de usuários.
 * Orquestra o fluxo: busca → validação → Keycloak → banco de dados.
 * Segue Dependency Inversion Principle (depende de Ports, não de implementações).
 */
@Service
@Slf4j
@RequiredArgsConstructor
public class UserUpdater {
    private final UserFindPort userFindPort;
    private final KeycloakFacade keycloakFacade;
    private final UpdateUserOrchestrator updateUserOrchestrator;
    private final EmailUpdateValidator emailUpdateValidator;
    private final EmailNormalizer emailNormalizer;

    @Transactional
    public void updateUser(String keycloakId, UserUpdateRequestDTO dto) {
        log.info("Iniciando atualização de usuário com ID: {}", keycloakId);

        // 1. Buscar usuário
        User user = userFindPort.findByKeycloakId(keycloakId)
                .orElseThrow(() -> {
                    log.warn("Usuário com ID '{}' não encontrado.", keycloakId);
                    return new UserNotFoundException(keycloakId);
                });

        // 2. Validar novo email
        String normalizedEmail = emailNormalizer.normalize(dto.getEmail());
        ValidationResult emailValidation = emailUpdateValidator.validate(user, normalizedEmail);
        if (!emailValidation.isValid()) {
            throw new DuplicateEmailException(emailValidation.errorMessage());
        }

        // 3. Atualizar no Keycloak
        keycloakFacade.updateUser(keycloakId, normalizedEmail);
        log.info("Usuário atualizado no Keycloak.");

        // 4. Atualizar email usando método de domínio
        user.updateEmail(normalizedEmail);

        // 5. Persistir no banco
        updateUserOrchestrator.updateWithConsistency(user);

        log.info("Usuário '{}' atualizado com sucesso.", user.getUserName());
    }
}
