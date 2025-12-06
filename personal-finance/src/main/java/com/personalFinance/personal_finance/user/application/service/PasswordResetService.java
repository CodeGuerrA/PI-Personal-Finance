package com.personalFinance.personal_finance.user.application.service;

import com.personalFinance.personal_finance.user.api.dto.request.ResetPasswordWithCodeRequestDTO;
import com.personalFinance.personal_finance.user.domain.entity.User;
import com.personalFinance.personal_finance.user.domain.exception.UserNotFoundException;
import com.personalFinance.personal_finance.user.domain.port.UserFindPort;
import com.personalFinance.personal_finance.user.infrastructure.external.keycloak.facade.KeycloakFacade;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

/**
 * Serviço responsável pela redefinição de senha após a validação do código.
 * Responsabilidade Única: Validar o código e executar a redefinição de senha.
 */
@Slf4j
@Service
@RequiredArgsConstructor
public class PasswordResetService {

    private final UserFindPort userFindPort;
    private final PasswordRecoveryCodeManager codeManager;
    private final KeycloakFacade keycloakFacade;

    /**
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

        // Busca usuário por email de forma otimizada
        User user = userFindPort.findByEmail(dto.getEmail())
                .orElseThrow(() -> new UserNotFoundException("Usuário não encontrado"));

        // Redefine a senha no Keycloak
        keycloakFacade.resetPassword(user.getKeycloakId(), dto.getNewPassword());
        log.info("Senha redefinida com sucesso para o usuário: {}", user.getEmail());

        // Remove o código após uso bem-sucedido
        codeManager.removeCode(dto.getEmail());
    }
}
