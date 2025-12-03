package com.personalFinance.personal_finance.investment.domain.entity;

/**
 * Enum representando os tipos de investimento disponíveis.
 */
public enum TipoInvestimento {
    CRIPTO("Criptomoedas"),
    ACAO("Ações"),
    FUNDO("Fundos de Investimento"),
    RENDA_FIXA("Renda Fixa"),
    TESOURO_DIRETO("Tesouro Direto"),
    CDB("CDB");

    private final String name;

    TipoInvestimento(String name) {
        this.name = name;
    }

    public String getName() {
        return name;
    }
}
