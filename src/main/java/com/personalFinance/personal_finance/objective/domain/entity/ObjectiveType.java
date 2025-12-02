package com.personalFinance.personal_finance.objective.domain.entity;

/**
 * Enum representando os tipos de meta/objetivo financeiro.
 */
public enum ObjectiveType {
    LIMITE_CATEGORIA("Limite de Categoria", "Orçamento máximo para uma categoria"),
    META_ECONOMIA_MES("Meta de Economia Mensal", "Quanto quer economizar no mês"),
    META_INVESTIMENTO("Meta de Investimento", "Quanto quer investir no período");

    private final String name;
    private final String description;

    ObjectiveType(String name, String description) {
        this.name = name;
        this.description = description;
    }

    public String getName() {
        return name;
    }

    public String getDescription() {
        return description;
    }
}
