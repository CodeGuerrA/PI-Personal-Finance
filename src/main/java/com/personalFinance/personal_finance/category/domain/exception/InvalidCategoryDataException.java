package com.personalFinance.personal_finance.category.domain.exception;

/**
 * Exceção lançada quando os dados da categoria são inválidos.
 */
public class InvalidCategoryDataException extends RuntimeException {

    public InvalidCategoryDataException(String message) {
        super(message);
    }
}