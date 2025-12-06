package com.personalFinance.personal_finance.shared.validator;

/**
 * Representa o resultado de uma validação.
 * Usado por Validator e BiValidator para retornar o status da validação.
 */
public record ValidationResult(boolean isValid, String errorMessage) {

    /**
     * Cria um resultado de validação bem-sucedida.
     */
    public static ValidationResult valid() {
        return new ValidationResult(true, null);
    }

    /**
     * Cria um resultado de validação com erro.
     *
     * @param message Mensagem de erro descritiva
     */
    public static ValidationResult invalid(String message) {
        return new ValidationResult(false, message);
    }
}
