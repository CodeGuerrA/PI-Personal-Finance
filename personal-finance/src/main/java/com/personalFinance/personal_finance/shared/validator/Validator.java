package com.personalFinance.personal_finance.shared.validator;

/**
 * Interface genérica para validação de valores únicos.
 *
 * @param <T> O tipo do valor a ser validado
 */
public interface Validator<T> {

    /**
     * Valida um valor e retorna o resultado da validação.
     *
     * @param value O valor a ser validado
     * @return ValidationResult indicando se a validação foi bem-sucedida
     */
    ValidationResult validate(T value);
}
