package com.personalFinance.personal_finance.investment.domain.entity;

public enum TipoMovimentacao {
    COMPRA("Aquisição de ativos"),
    VENDA("Venda de ativos"),
    DIVIDENDO("Recebimento de dividendos"),
    RENDIMENTO("Rendimentos (juros)"),
    AJUSTE("Correções manuais");

    private final String descricao;

    TipoMovimentacao(String descricao) {
        this.descricao = descricao;
    }

    public String getDescricao() {
        return descricao;
    }
}
