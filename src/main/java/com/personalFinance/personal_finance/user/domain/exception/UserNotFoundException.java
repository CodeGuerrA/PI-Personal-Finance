package com.personalFinance.personal_finance.user.domain.exception;

public class UserNotFoundException extends RuntimeException {
    public UserNotFoundException(String keycloakId) {
        super("Usuário com ID '" + keycloakId + "' não encontrado.");
    }

    public UserNotFoundException(String message, Throwable cause) {
        super(message, cause);
    }
}
