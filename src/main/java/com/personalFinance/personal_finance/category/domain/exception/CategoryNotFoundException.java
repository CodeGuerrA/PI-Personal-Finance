package com.personalFinance.personal_finance.category.domain.exception;

/**
 * Exceção lançada quando uma categoria não é encontrada.
 */
public class CategoryNotFoundException extends RuntimeException {

    public CategoryNotFoundException(Long id) {
        super("Categoria com ID '" + id + "' não encontrada.");
    }

    public CategoryNotFoundException(String message) {
        super(message);
    }
}