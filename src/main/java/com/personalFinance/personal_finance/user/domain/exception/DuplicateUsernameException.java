package com.personalFinance.personal_finance.user.domain.exception;

public class DuplicateUsernameException extends RuntimeException {
    public DuplicateUsernameException(String username) {
        super("Já existe um usuário com o username: " + username);
    }
}
