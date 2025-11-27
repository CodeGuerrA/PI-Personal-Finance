package com.personalFinance.personal_finance.shared.validator;

/**
 * Interface genérica para validação de dois valores relacionados.
 *
 * @param <T> O tipo do primeiro valor a ser validado
 * @param <U> O tipo do segundo valor a ser validado
 */
public interface BiValidator<T, U> {

    /**
     * Valida dois valores relacionados e retorna o resultado da validação.
     *
     * @param firstValue O primeiro valor a ser validado
     * @param secondValue O segundo valor a ser validado
     * @return ValidationResult indicando se a validação foi bem-sucedida
     */
    ValidationResult validate(T firstValue, U secondValue);
}

