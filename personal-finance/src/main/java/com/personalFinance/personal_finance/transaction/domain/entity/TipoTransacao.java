package com.personalFinance.personal_finance.transaction.domain.entity;

/**
 * Tipo da transação financeira.
 */
public enum TipoTransacao {
    RECEITA("Receita"),
    DESPESA("Despesa");

    private final String name;

    TipoTransacao(String name) {
        this.name = name;
    }

    public String getName() {
        return name;
    }
}