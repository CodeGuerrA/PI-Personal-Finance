package com.personalFinance.personal_finance.user.infrastructure.external.email;

import com.personalFinance.personal_finance.user.infrastructure.external.email.facade.UserNotificationService;
import com.personalFinance.personal_finance.user.infrastructure.external.email.strategy.PasswordChangedEmailStrategy;
import com.personalFinance.personal_finance.user.infrastructure.external.email.strategy.RecoveryCodeEmailStrategy;
import com.personalFinance.personal_finance.user.infrastructure.external.email.strategy.UserNotificationStrategy;
import com.personalFinance.personal_finance.user.infrastructure.external.email.strategy.WelcomeEmailStrategy;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.ArgumentCaptor;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.Spy;
import org.mockito.junit.jupiter.MockitoExtension;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.*;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
@DisplayName("Testes para UserNotificationEmailService")
class UserNotificationEmailServiceTest {

    @Mock
    private EmailStrategyExecutor emailStrategyExecutor;

    @Spy
    private WelcomeEmailStrategy welcomeEmailStrategy;

    @Spy
    private PasswordChangedEmailStrategy passwordChangedEmailStrategy;

    @Spy
    private RecoveryCodeEmailStrategy recoveryCodeEmailStrategy;

    @InjectMocks
    private UserNotificationService notificationEmailService;

    private String testEmail;
    private String testFirstName;
    private String testUsername;
    private String testTemporaryPassword;
    private String testRecoveryCode;

    @BeforeEach
    void setUp() {
        testEmail = "joao@email.com";
        testFirstName = "João";
        testUsername = "joao.silva";
        testTemporaryPassword = "Temp1234!";
        testRecoveryCode = "123456";
    }

    @Test
    @DisplayName("Deve enviar email de boas-vindas com informações corretas")
    void deveEnviarEmailDeBoasVindasComInformacoesCorretas() {
        // Arrange
        ArgumentCaptor<String> emailCaptor = ArgumentCaptor.forClass(String.class);
        ArgumentCaptor<UserNotificationStrategy> strategyCaptor = ArgumentCaptor.forClass(UserNotificationStrategy.class);

        doNothing().when(emailStrategyExecutor)
                .execute(emailCaptor.capture(), strategyCaptor.capture(), any());

        // Act
        notificationEmailService.sendWelcomeEmail(testEmail, testUsername, testTemporaryPassword);

        // Assert
        assertEquals(testEmail, emailCaptor.getValue(), "Email destinatário deve estar correto");
        assertInstanceOf(WelcomeEmailStrategy.class, strategyCaptor.getValue(), "Deve usar WelcomeEmailStrategy");

        verify(emailStrategyExecutor).execute(eq(testEmail), any(WelcomeEmailStrategy.class), any());
    }

    @Test
    @DisplayName("Deve enviar email de senha alterada com informações corretas")
    void deveEnviarEmailDeSenhaAlteradaComInformacoesCorretas() {
        // Arrange
        ArgumentCaptor<String> emailCaptor = ArgumentCaptor.forClass(String.class);
        ArgumentCaptor<UserNotificationStrategy> strategyCaptor = ArgumentCaptor.forClass(UserNotificationStrategy.class);

        doNothing().when(emailStrategyExecutor)
                .execute(emailCaptor.capture(), strategyCaptor.capture(), any());

        // Act
        notificationEmailService.sendPasswordChangedNotification(testEmail, testFirstName);

        // Assert
        assertEquals(testEmail, emailCaptor.getValue(), "Email destinatário deve estar correto");
        assertInstanceOf(PasswordChangedEmailStrategy.class, strategyCaptor.getValue(), "Deve usar PasswordChangedEmailStrategy");

        verify(emailStrategyExecutor).execute(eq(testEmail), any(PasswordChangedEmailStrategy.class), any());
    }

    @Test
    @DisplayName("Deve enviar email de recuperação com código correto")
    void deveEnviarEmailDeRecuperacaoComCodigoCorreto() {
        // Arrange
        ArgumentCaptor<String> emailCaptor = ArgumentCaptor.forClass(String.class);
        ArgumentCaptor<UserNotificationStrategy> strategyCaptor = ArgumentCaptor.forClass(UserNotificationStrategy.class);

        doNothing().when(emailStrategyExecutor)
                .execute(emailCaptor.capture(), strategyCaptor.capture(), any());

        // Act
        notificationEmailService.sendRecoveryCode(testEmail, testFirstName, testRecoveryCode);

        // Assert
        assertEquals(testEmail, emailCaptor.getValue(), "Email destinatário deve estar correto");
        assertInstanceOf(RecoveryCodeEmailStrategy.class, strategyCaptor.getValue(), "Deve usar RecoveryCodeEmailStrategy");

        verify(emailStrategyExecutor).execute(eq(testEmail), any(RecoveryCodeEmailStrategy.class), any());
    }
}
