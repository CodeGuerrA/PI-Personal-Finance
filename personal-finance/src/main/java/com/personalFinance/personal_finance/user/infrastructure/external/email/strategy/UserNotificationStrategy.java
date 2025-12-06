package com.personalFinance.personal_finance.user.infrastructure.external.email.strategy;

public interface UserNotificationStrategy<T> {
    /**
     * Retorna assunto do email
     */
    String getSubject();

    /**
     * Retorna conteúdo HTML do email
     */
    String buildContent(T dto);
}
