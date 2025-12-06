package com.personalFinance.personal_finance.transaction.domain.entity;

/**
 * Frequência da transação recorrente.
 */
public enum FrequenciaRecorrencia {
    DIARIA("Diária"),
    SEMANAL("Semanal"),
    MENSAL("Mensal"),
    ANUAL("Anual");

    private final String name;

    FrequenciaRecorrencia(String name) {
        this.name = name;
    }

    public String getName() {
        return name;
    }
}