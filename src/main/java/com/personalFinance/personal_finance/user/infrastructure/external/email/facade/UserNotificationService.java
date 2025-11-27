package com.personalFinance.personal_finance.user.infrastructure.external.email.facade;

import com.personalFinance.personal_finance.user.domain.port.UserNotificationPort;
import com.personalFinance.personal_finance.user.infrastructure.external.email.EmailStrategyExecutor;
import com.personalFinance.personal_finance.user.infrastructure.external.email.dto.PasswordChangedEmailDTO;
import com.personalFinance.personal_finance.user.infrastructure.external.email.dto.RecoveryCodeEmailDTO;
import com.personalFinance.personal_finance.user.infrastructure.external.email.dto.WelcomeEmailDTO;
import com.personalFinance.personal_finance.user.infrastructure.external.email.strategy.PasswordChangedEmailStrategy;
import com.personalFinance.personal_finance.user.infrastructure.external.email.strategy.RecoveryCodeEmailStrategy;
import com.personalFinance.personal_finance.user.infrastructure.external.email.strategy.WelcomeEmailStrategy;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

/**
 * Implementação do port de notificação usando estratégia de email.
 * Adapter que conecta o domain layer com a infraestrutura de email.
 */
@Slf4j
@Service
@RequiredArgsConstructor
public class UserNotificationService implements UserNotificationPort {
    private final EmailStrategyExecutor emailStrategyExecutor;
    private final WelcomeEmailStrategy welcomeEmailStrategy;
    private final PasswordChangedEmailStrategy passwordChangedEmailStrategy;
    private final RecoveryCodeEmailStrategy recoveryCodeEmailStrategy;

    @Override
    public void sendWelcomeEmail(String email, String username, String temporaryPassword) {
        WelcomeEmailDTO dto = new WelcomeEmailDTO(username, temporaryPassword);
        emailStrategyExecutor.execute(email, welcomeEmailStrategy, dto);
    }

    @Override
    public void sendPasswordChangedNotification(String email, String firstName) {
        PasswordChangedEmailDTO dto = new PasswordChangedEmailDTO(firstName);
        emailStrategyExecutor.execute(email, passwordChangedEmailStrategy, dto);
    }

    @Override
    public void sendRecoveryCode(String email, String firstName, String code) {
        RecoveryCodeEmailDTO dto = new RecoveryCodeEmailDTO(firstName, code);
        emailStrategyExecutor.execute(email, recoveryCodeEmailStrategy, dto);
    }
}

