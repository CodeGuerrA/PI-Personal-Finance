package com.personalFinance.personal_finance.investment.domain.exception;

/**
 * Exceção lançada quando um investimento não é encontrado.
 */
public class InvestmentNotFoundException extends RuntimeException {
    public InvestmentNotFoundException(Long investmentId) {
        super("Investimento com ID '" + investmentId + "' não encontrado.");
    }

    public InvestmentNotFoundException(String message) {
        super(message);
    }
}
