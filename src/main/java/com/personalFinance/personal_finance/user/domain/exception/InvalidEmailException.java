package com.personalFinance.personal_finance.user.domain.exception;

public class InvalidEmailException extends RuntimeException {
    public InvalidEmailException(String email) {
        super("Email inválido: " + email);
    }

    public InvalidEmailException(String message, Throwable cause) {
        super(message, cause);
    }
}
