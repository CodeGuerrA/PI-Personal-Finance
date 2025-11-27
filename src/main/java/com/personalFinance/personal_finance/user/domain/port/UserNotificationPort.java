package com.personalFinance.personal_finance.user.domain.port;

/**
 * Port para notificações de usuários.
 * Define o contrato para envio de notificações sem depender de tecnologia específica.
 * Implementações podem ser email, SMS, push notifications, etc.
 */
public interface UserNotificationPort {

    /**
     * Envia email de boas-vindas para novo usuário.
     *
     * @param email email do destinatário
     * @param username nome de usuário gerado
     * @param temporaryPassword senha temporária gerada
     */
    void sendWelcomeEmail(String email, String username, String temporaryPassword);

    /**
     * Envia notificação de alteração de senha.
     *
     * @param email email do destinatário
     * @param firstName primeiro nome do usuário
     */
    void sendPasswordChangedNotification(String email, String firstName);

    /**
     * Envia código de recuperação de senha.
     *
     * @param email email do destinatário
     * @param firstName primeiro nome do usuário
     * @param recoveryCode código de verificação
     */
    void sendRecoveryCode(String email, String firstName, String recoveryCode);
}
