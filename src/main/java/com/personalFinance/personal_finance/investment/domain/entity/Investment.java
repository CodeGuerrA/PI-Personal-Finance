package com.personalFinance.personal_finance.investment.domain.entity;

import com.personalFinance.personal_finance.user.domain.entity.User;
import jakarta.persistence.*;
import lombok.*;

import java.math.BigDecimal;
import java.math.RoundingMode;
import java.time.LocalDate;

/**
 * Rich Domain Model: Entidade Investment (Investimento).
 * Responsabilidade: Manter estado do investimento financeiro.
 * Segue princípios SOLID e Clean Code.
 * SRP: Validações e cálculos complexos delegados para Domain Services.
 */
@Entity
@Table(name = "investments")
@Getter
@NoArgsConstructor(access = AccessLevel.PROTECTED)
@AllArgsConstructor(access = AccessLevel.PRIVATE)
@Builder(access = AccessLevel.PRIVATE)
@EqualsAndHashCode(onlyExplicitlyIncluded = true)
@ToString(of = {"id", "nomeAtivo", "simbolo", "tipoInvestimento"})
public class Investment {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @EqualsAndHashCode.Include
    private Long id;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "usuario_id", nullable = false)
    private User usuario;

    @Enumerated(EnumType.STRING)
    @Column(name = "tipo_investimento", nullable = false, length = 50)
    private TipoInvestimento tipoInvestimento;

    @Column(name = "nome_ativo", nullable = false, length = 200)
    private String nomeAtivo;

    @Column(name = "simbolo", nullable = false, length = 20)
    private String simbolo;

    @Column(name = "quantidade", nullable = false, precision = 18, scale = 8)
    private BigDecimal quantidade;

    @Column(name = "valor_compra", nullable = false, precision = 15, scale = 2)
    private BigDecimal valorCompra;

    @Column(name = "valor_total_investido", nullable = false, precision = 15, scale = 2)
    private BigDecimal valorTotalInvestido;

    @Column(name = "data_compra", nullable = false)
    private LocalDate dataCompra;

    @Column(name = "corretora", length = 100)
    private String corretora;

    @Column(name = "observacoes", length = 1000)
    private String observacoes;

    @Column(name = "ativo", nullable = false)
    private Boolean ativo;

    /**
     * Cotação atual do ativo (não persistida, calculada dinamicamente).
     * Será obtida de serviço externo de cotações.
     */
    @Transient
    private BigDecimal cotacaoAtual;

    /**
     * Factory method para criar novo investimento.
     * OCP: Aberto para extensão (novos tipos), fechado para modificação.
     * Validações delegadas para Validators (SRP).
     */
    public static Investment create(User usuario, TipoInvestimento tipoInvestimento,
                                   String nomeAtivo, String simbolo, BigDecimal quantidade,
                                   BigDecimal valorCompra, BigDecimal valorTotalInvestido,
                                   LocalDate dataCompra, String corretora, String observacoes) {
        return Investment.builder()
                .usuario(usuario)
                .tipoInvestimento(tipoInvestimento)
                .nomeAtivo(nomeAtivo)
                .simbolo(simbolo)
                .quantidade(quantidade)
                .valorCompra(valorCompra)
                .valorTotalInvestido(valorTotalInvestido)
                .dataCompra(dataCompra)
                .corretora(corretora)
                .observacoes(observacoes)
                .ativo(true)
                .build();
    }

    /**
     * Define a cotação atual do ativo (calculada externamente).
     */
    public void setCotacaoAtual(BigDecimal cotacaoAtual) {
        this.cotacaoAtual = cotacaoAtual != null ? cotacaoAtual : BigDecimal.ZERO;
    }

    /**
     * Calcula o valor atual do investimento.
     * Formula: quantidade × cotação atual
     */
    public BigDecimal calcularValorAtual() {
        if (cotacaoAtual == null || cotacaoAtual.compareTo(BigDecimal.ZERO) == 0) {
            return BigDecimal.ZERO;
        }
        return quantidade.multiply(cotacaoAtual).setScale(2, RoundingMode.HALF_UP);
    }

    /**
     * Calcula o lucro/prejuízo do investimento.
     * Formula: valorAtual - valorTotalInvestido
     */
    public BigDecimal calcularLucro() {
        BigDecimal valorAtual = calcularValorAtual();
        return valorAtual.subtract(valorTotalInvestido).setScale(2, RoundingMode.HALF_UP);
    }

    /**
     * Calcula a rentabilidade percentual do investimento.
     * Formula: (lucro / valorTotalInvestido) × 100
     */
    public BigDecimal calcularRentabilidade() {
        if (valorTotalInvestido.compareTo(BigDecimal.ZERO) == 0) {
            return BigDecimal.ZERO;
        }
        BigDecimal lucro = calcularLucro();
        return lucro.divide(valorTotalInvestido, 4, RoundingMode.HALF_UP)
                    .multiply(BigDecimal.valueOf(100))
                    .setScale(2, RoundingMode.HALF_UP);
    }

    /**
     * Desativa o investimento (indica que não possui mais o ativo).
     */
    public void desativar() {
        this.ativo = false;
    }

    /**
     * Reativa o investimento.
     */
    public void reativar() {
        this.ativo = true;
    }

    /**
     * Atualiza a quantidade do investimento.
     */
    public void atualizarQuantidade(BigDecimal novaQuantidade) {
        this.quantidade = novaQuantidade;
    }

    /**
     * Atualiza o valor médio de compra.
     */
    public void atualizarValorCompra(BigDecimal novoValorCompra) {
        this.valorCompra = novoValorCompra;
    }

    /**
     * Atualiza o valor total investido.
     */
    public void atualizarValorTotalInvestido(BigDecimal novoValorTotal) {
        this.valorTotalInvestido = novoValorTotal;
    }

    /**
     * Atualiza observações.
     */
    public void atualizarObservacoes(String novasObservacoes) {
        this.observacoes = novasObservacoes;
    }

    /**
     * Verifica se o investimento está ativo.
     */
    public boolean isAtivo() {
        return this.ativo;
    }

    /**
     * Verifica se o investimento é de um tipo específico.
     */
    public boolean isTipo(TipoInvestimento tipo) {
        return this.tipoInvestimento == tipo;
    }

    /**
     * Verifica se o investimento pertence a um usuário.
     * DRY: Encapsula verificação de propriedade.
     */
    public boolean pertenceAoUsuario(Long usuarioId) {
        return this.usuario != null && this.usuario.getId().equals(usuarioId);
    }
}
