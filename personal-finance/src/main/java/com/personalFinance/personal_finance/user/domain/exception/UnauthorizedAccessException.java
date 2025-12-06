package com.personalFinance.personal_finance.user.domain.exception;

/**
 * Exceção lançada quando um usuário tenta acessar recursos de outro usuário.
 */
public class UnauthorizedAccessException extends RuntimeException {
    public UnauthorizedAccessException() {
        super("Você não tem permissão para acessar este recurso");
    }

    public UnauthorizedAccessException(String message) {
        super(message);
    }
}
