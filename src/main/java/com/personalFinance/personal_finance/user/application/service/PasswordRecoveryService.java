package com.personalFinance.personal_finance.user.application.service;

import com.personalFinance.personal_finance.user.api.dto.request.ForgotPasswordRequestDTO;
import com.personalFinance.personal_finance.user.api.dto.request.ResetPasswordWithCodeRequestDTO;
import com.personalFinance.personal_finance.user.domain.entity.User;
import com.personalFinance.personal_finance.user.domain.exception.UserNotFoundException;
import com.personalFinance.personal_finance.user.domain.port.UserFindPort;
import com.personalFinance.personal_finance.user.domain.port.UserNotificationPort;
import com.personalFinance.personal_finance.user.infrastructure.external.keycloak.facade.KeycloakFacade;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

/**
 * Serviço para gerenciar recuperação de senha.
 * TODO: Refatorar seguindo SRP - separar em PasswordRecoveryRequestService e PasswordResetService
 */
@Slf4j
@Service
@RequiredArgsConstructor
public class PasswordRecoveryService {

    private final UserFindPort userFindPort;
    private final PasswordRecoveryCodeManager codeManager;
    private final UserNotificationPort userNotificationPort;
    private final KeycloakFacade keycloakFacade;

    /**
     * Solicita recuperação de senha enviando código por email.
     */
    @Transactional(readOnly = true)
    public void requestPasswordRecovery(ForgotPasswordRequestDTO dto) {
        log.info("Solicitação de recuperação de senha para o email: {}", dto.getEmail());

        try {
            // Busca usuário por email
            //fazer query para buscar por email
            User user = userFindPort.findByEmail(dto.getEmail())
                    .orElseThrow(() -> new UserNotFoundException("Usuário não encontrado"));

            // Gera código de 6 dígitos
            String code = codeManager.generateCode(dto.getEmail());

            // Envia código por email
            userNotificationPort.sendRecoveryCode(user.getEmail(), user.getFirstName(), code);

            log.info("Código de recuperação enviado com sucesso para: {}", dto.getEmail());

        } catch (UserNotFoundException e) {
            // Por segurança, não revela se o email existe ou não
            log.warn("Tentativa de recuperação para email não cadastrado: {}", dto.getEmail());
            // Não lança exceção para não revelar se o email existe
        }
    }

    /**
     * aqui passamos o codigo para redefinir a senha
     * Redefine a senha usando o código de verificação.
     */
    @Transactional
    public void resetPasswordWithCode(ResetPasswordWithCodeRequestDTO dto) {
        log.info("Tentativa de redefinição de senha para o email: {}", dto.getEmail());

        // Valida o código
        if (!codeManager.validateCode(dto.getEmail(), dto.getCode())) {
            log.warn("Código inválido ou expirado para o email: {}", dto.getEmail());
            throw new RuntimeException("Código de verificação inválido ou expirado");
        }

        // Busca usuário por email
        User user = userFindPort.findAll().stream()
                .filter(u -> u.getEmail().equalsIgnoreCase(dto.getEmail()))
                .findFirst()
                .orElseThrow(() -> new UserNotFoundException("Usuário não encontrado"));

        // Redefine a senha no Keycloak
        try {
            keycloakFacade.resetPassword(user.getKeycloakId(), dto.getNewPassword());
            log.info("Senha redefinida com sucesso para o usuário: {}", user.getEmail());

            // Remove o código após uso bem-sucedido
            codeManager.removeCode(dto.getEmail());

        } catch (Exception e) {
            log.error("Erro ao redefinir senha para o usuário {}: {}", user.getEmail(), e.getMessage(), e);
            throw new RuntimeException("Erro ao redefinir senha", e);
        }
    }
}
