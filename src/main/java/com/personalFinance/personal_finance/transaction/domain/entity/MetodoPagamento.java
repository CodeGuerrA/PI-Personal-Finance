package com.personalFinance.personal_finance.transaction.domain.entity;

/**
 * Método de pagamento da transação.
 */
public enum MetodoPagamento {
    DINHEIRO("Dinheiro"),
    CARTAO_CREDITO("Cartão de Crédito"),
    CARTAO_DEBITO("Cartão de Débito"),
    PIX("PIX");

    private final String name;

    MetodoPagamento(String name) {
        this.name = name;
    }

    public String getName() {
        return name;
    }
}