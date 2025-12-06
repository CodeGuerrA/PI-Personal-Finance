package com.personalFinance.personal_finance.user.domain.exception;

public class UserPersistenceException extends RuntimeException {
    public UserPersistenceException(String message, Throwable cause) {
        super(message, cause);
    }

    public UserPersistenceException(String message) {
        super(message);
    }
}
