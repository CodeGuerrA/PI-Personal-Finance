package com.personalFinance.personal_finance.investment.domain.entity;

import com.personalFinance.personal_finance.user.domain.entity.User;
import jakarta.persistence.*;
import lombok.*;

import java.math.BigDecimal;
import java.time.LocalDate;

/**
 * Rich Domain Model: Entidade InvestmentMovement (Movimentação de Investimento).
 * Responsabilidade: Registrar histórico detalhado de operações com investimentos.
 * Segue princípios SOLID e Clean Code.
 */
@Entity
@Table(name = "investment_movements")
@Getter
@NoArgsConstructor(access = AccessLevel.PROTECTED)
@AllArgsConstructor(access = AccessLevel.PRIVATE)
@Builder(access = AccessLevel.PRIVATE)
@EqualsAndHashCode(onlyExplicitlyIncluded = true)
@ToString(of = {"id", "tipoMovimentacao", "dataMovimentacao", "valorTotal"})
public class InvestmentMovement {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @EqualsAndHashCode.Include
    private Long id;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "investment_id", nullable = false)
    private Investment investment;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "usuario_id", nullable = false)
    private User usuario;

    @Enumerated(EnumType.STRING)
    @Column(name = "tipo_movimentacao", nullable = false, length = 20)
    private TipoMovimentacao tipoMovimentacao;

    @Column(name = "quantidade", nullable = false, precision = 18, scale = 8)
    private BigDecimal quantidade;

    @Column(name = "valor_unitario", nullable = false, precision = 15, scale = 2)
    private BigDecimal valorUnitario;

    @Column(name = "valor_total", nullable = false, precision = 15, scale = 2)
    private BigDecimal valorTotal;

    @Column(name = "taxas", precision = 15, scale = 2)
    private BigDecimal taxas;

    @Column(name = "data_movimentacao", nullable = false)
    private LocalDate dataMovimentacao;

    @Column(name = "observacoes", length = 1000)
    private String observacoes;

    /**
     * Factory method para criar nova movimentação de investimento.
     * Validações delegadas para Validators (SRP).
     */
    public static InvestmentMovement create(Investment investment, User usuario,
                                           TipoMovimentacao tipoMovimentacao,
                                           BigDecimal quantidade, BigDecimal valorUnitario,
                                           BigDecimal valorTotal, BigDecimal taxas,
                                           LocalDate dataMovimentacao, String observacoes) {
        return InvestmentMovement.builder()
                .investment(investment)
                .usuario(usuario)
                .tipoMovimentacao(tipoMovimentacao)
                .quantidade(quantidade)
                .valorUnitario(valorUnitario)
                .valorTotal(valorTotal)
                .taxas(taxas != null ? taxas : BigDecimal.ZERO)
                .dataMovimentacao(dataMovimentacao)
                .observacoes(observacoes)
                .build();
    }

    /**
     * Verifica se é uma movimentação de compra.
     */
    public boolean isCompra() {
        return tipoMovimentacao == TipoMovimentacao.COMPRA;
    }

    /**
     * Verifica se é uma movimentação de venda.
     */
    public boolean isVenda() {
        return tipoMovimentacao == TipoMovimentacao.VENDA;
    }

    /**
     * Verifica se é uma movimentação que gera receita (VENDA, DIVIDENDO, RENDIMENTO).
     */
    public boolean geraReceita() {
        return tipoMovimentacao == TipoMovimentacao.VENDA ||
               tipoMovimentacao == TipoMovimentacao.DIVIDENDO ||
               tipoMovimentacao == TipoMovimentacao.RENDIMENTO;
    }

    /**
     * Verifica se é uma movimentação que gera despesa (COMPRA).
     */
    public boolean geraDespesa() {
        return tipoMovimentacao == TipoMovimentacao.COMPRA;
    }

    /**
     * Verifica se a movimentação pertence a um usuário.
     */
    public boolean pertenceAoUsuario(Long usuarioId) {
        return this.usuario != null && this.usuario.getId().equals(usuarioId);
    }

    /**
     * Atualiza observações.
     */
    public void atualizarObservacoes(String novasObservacoes) {
        this.observacoes = novasObservacoes;
    }

    /**
     * Atualiza a quantidade.
     */
    public void atualizarQuantidade(BigDecimal novaQuantidade) {
        this.quantidade = novaQuantidade;
    }

    /**
     * Atualiza o valor unitário.
     */
    public void atualizarValorUnitario(BigDecimal novoValorUnitario) {
        this.valorUnitario = novoValorUnitario;
    }

    /**
     * Atualiza o valor total.
     */
    public void atualizarValorTotal(BigDecimal novoValorTotal) {
        this.valorTotal = novoValorTotal;
    }

    /**
     * Atualiza as taxas.
     */
    public void atualizarTaxas(BigDecimal novasTaxas) {
        this.taxas = novasTaxas;
    }
}
