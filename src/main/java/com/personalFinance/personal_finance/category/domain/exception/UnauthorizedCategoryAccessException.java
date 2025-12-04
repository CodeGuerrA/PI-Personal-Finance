package com.personalFinance.personal_finance.category.domain.exception;

/**
 * Exceção lançada quando o usuário tenta acessar uma categoria que não lhe pertence.
 */
public class UnauthorizedCategoryAccessException extends RuntimeException {

    public UnauthorizedCategoryAccessException() {
        super("Você não tem permissão para acessar esta categoria.");
    }

    public UnauthorizedCategoryAccessException(String message) {
        super(message);
    }
}
