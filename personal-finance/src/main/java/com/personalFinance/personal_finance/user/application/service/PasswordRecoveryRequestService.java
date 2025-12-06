package com.personalFinance.personal_finance.user.application.service;

import com.personalFinance.personal_finance.user.api.dto.request.ForgotPasswordRequestDTO;
import com.personalFinance.personal_finance.user.domain.port.UserFindPort;
import com.personalFinance.personal_finance.user.domain.port.UserNotificationPort;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

/**
 * Serviço responsável pela solicitação inicial de recuperação de senha.
 * Responsabilidade Única: Gerar e enviar o código de recuperação.
 */
@Slf4j
@Service
@RequiredArgsConstructor
public class PasswordRecoveryRequestService {

    private final UserFindPort userFindPort;
    private final PasswordRecoveryCodeManager codeManager;
    private final UserNotificationPort userNotificationPort;

    /**
     * Solicita recuperação de senha enviando código por email.
     */
    @Transactional(readOnly = true)
    public void requestPasswordRecovery(ForgotPasswordRequestDTO dto) {
        log.info("Solicitação de recuperação de senha para o email: {}", dto.getEmail());

        // Mantém a lógica de segurança de negócio: não lança exceção se o usuário não for encontrado.
        userFindPort.findByEmail(dto.getEmail())
                .ifPresentOrElse(user -> {
                    // Gera código de 6 dígitos
                    String code = codeManager.generateCode(dto.getEmail());

                    // Envia código por email
                    userNotificationPort.sendRecoveryCode(user.getEmail(), user.getFirstName(), code);

                    log.info("Código de recuperação enviado com sucesso para: {}", dto.getEmail());
                }, () -> {
                    // Por segurança, não revela se o email existe ou não
                    log.warn("Tentativa de recuperação para email não cadastrado: {}", dto.getEmail());
                    // Não lança exceção para não revelar se o email existe
                });
    }
}
