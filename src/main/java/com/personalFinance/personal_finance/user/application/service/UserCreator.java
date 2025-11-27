package com.personalFinance.personal_finance.user.application.service;

import com.personalFinance.personal_finance.shared.validator.ValidationResult;
import com.personalFinance.personal_finance.user.api.dto.request.UserCreateRequestDTO;
import com.personalFinance.personal_finance.user.api.dto.response.KeycloakUserResponseDTO;
import com.personalFinance.personal_finance.user.application.orchestrator.SaveUserOrchestrator;
import com.personalFinance.personal_finance.user.domain.entity.User;
import com.personalFinance.personal_finance.user.domain.exception.DuplicateCpfException;
import com.personalFinance.personal_finance.user.domain.exception.DuplicateEmailException;
import com.personalFinance.personal_finance.user.domain.port.UserNotificationPort;
import com.personalFinance.personal_finance.user.domain.service.validation.*;
import com.personalFinance.personal_finance.user.infrastructure.external.keycloak.facade.KeycloakFacade;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

/**
 * Serviço de aplicação para criação de usuários.
 * Orquestra o fluxo completo: validação → Keycloak → banco de dados → email.
 * Segue Single Responsibility Principle.
 */
@Slf4j
@Service
@RequiredArgsConstructor
public class UserCreator {
    private final CPFValidator cpfValidator;
    private final EmailValidator emailValidator;
    private final EmailNormalizer emailNormalizer;
    private final CpfNormalizer cpfNormalizer;
    private final UsernameGenerator usernameGenerator;
    private final KeycloakFacade keycloakFacade;
    private final SaveUserOrchestrator saveUserOrchestrator;
    private final UserNotificationPort userNotificationPort;

    @Transactional
    public User createUser(UserCreateRequestDTO dto) {
        log.info("Iniciando criação de usuário. Email: {} | CPF: {}", dto.getEmail(), dto.getCpf());

        // 1. Validar Email
        String normalizedEmail = emailNormalizer.normalize(dto.getEmail());
        log.debug("Email normalizado: {}", normalizedEmail);
        ValidationResult emailValidation = emailValidator.validate(normalizedEmail);
        if (!emailValidation.isValid()) {
            throw new DuplicateEmailException(emailValidation.errorMessage());
        }

        // 2. Validar CPF
        String normalizedCpf = cpfNormalizer.normalize(dto.getCpf());
        log.debug("CPF normalizado: {}", normalizedCpf);
        ValidationResult cpfValidation = cpfValidator.validate(normalizedCpf);
        if (!cpfValidation.isValid()) {
            throw new DuplicateCpfException(cpfValidation.errorMessage());
        }

        // 3. Gerar username
        String username = usernameGenerator.generateUsername(dto.getFirstName(), dto.getLastName());
        log.debug("Username gerado: {}", username);

        // 4. Criar usuário no Keycloak
        KeycloakUserResponseDTO keycloakResponse = keycloakFacade.createUser(
                username,
                normalizedEmail,
                dto.getFirstName(),
                dto.getLastName()
        );
        log.info("Usuário criado no Keycloak com ID: {}", keycloakResponse.getUserId());

        // 5. Criar entidade de domínio usando factory method (Rich Domain Model)
        User user = User.create(
                username,
                dto.getFirstName(),
                dto.getLastName(),
                normalizedEmail,
                normalizedCpf,
                keycloakResponse.getUserId()
        );

        // 6. Salvar no banco com rollback automático em caso de falha
        User savedUser = saveUserOrchestrator.saveWithRollback(user, keycloakResponse.getUserId());

        // 7. Enviar email de boas-vindas
        userNotificationPort.sendWelcomeEmail(normalizedEmail, username, keycloakResponse.getTemporaryPassword());
        log.info("Email de boas-vindas enviado para: {}", normalizedEmail);

        return savedUser;
    }
}
