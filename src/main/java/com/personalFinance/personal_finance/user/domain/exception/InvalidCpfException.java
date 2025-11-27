package com.personalFinance.personal_finance.user.domain.exception;

/**
 * Exceção lançada quando um CPF inválido é fornecido.
 */
public class InvalidCpfException extends RuntimeException {
    public InvalidCpfException(String cpf) {
        super("CPF '" + cpf + "' é inválido");
    }

    public InvalidCpfException(String message, String cpf) {
        super(message);
    }
}
