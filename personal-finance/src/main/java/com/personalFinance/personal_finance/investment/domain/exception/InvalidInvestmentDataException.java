package com.personalFinance.personal_finance.investment.domain.exception;

/**
 * Exceção lançada quando os dados do investimento são inválidos.
 */
public class InvalidInvestmentDataException extends RuntimeException {
    public InvalidInvestmentDataException(String message) {
        super(message);
    }
}
