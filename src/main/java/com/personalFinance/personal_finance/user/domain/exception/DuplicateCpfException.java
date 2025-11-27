package com.personalFinance.personal_finance.user.domain.exception;

/**
 * Exceção lançada quando há tentativa de cadastrar um CPF já existente no sistema.
 */
public class DuplicateCpfException extends RuntimeException {
    public DuplicateCpfException(String cpf) {
        super("CPF '" + cpf + "' já está cadastrado no sistema");
    }
}
