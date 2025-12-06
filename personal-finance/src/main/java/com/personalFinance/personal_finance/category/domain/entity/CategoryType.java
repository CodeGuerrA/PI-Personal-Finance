package com.personalFinance.personal_finance.category.domain.entity;

/**
 * Enum representando o tipo de categoria (Receita ou Despesa).
 */
public enum CategoryType {
    RECEITA("Receita"),
    DESPESA("Despesa");

    private final String description;

    CategoryType(String description) {
        this.description = description;
    }

    public String getDescription() {
        return description;
    }
}
