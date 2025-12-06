package com.personalFinance.personal_finance.user.domain.exception;

public class DuplicateEmailException extends RuntimeException {
    public DuplicateEmailException(String email) {
        super("Já existe um usuário com o email: " + email);
    }
}
