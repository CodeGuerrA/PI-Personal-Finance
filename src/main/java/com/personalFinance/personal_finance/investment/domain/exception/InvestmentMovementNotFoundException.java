package com.personalFinance.personal_finance.investment.domain.exception;

/**
 * Exceção lançada quando uma movimentação de investimento não é encontrada.
 */
public class InvestmentMovementNotFoundException extends RuntimeException {

    public InvestmentMovementNotFoundException(String message) {
        super(message);
    }

    public static InvestmentMovementNotFoundException withId(Long id) {
        return new InvestmentMovementNotFoundException(
            "Movimentação de investimento com ID " + id + " não encontrada"
        );
    }
}
