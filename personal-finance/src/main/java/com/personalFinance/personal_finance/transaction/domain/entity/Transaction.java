package com.personalFinance.personal_finance.transaction.domain.entity;

import com.personalFinance.personal_finance.category.domain.entity.Category;
import com.personalFinance.personal_finance.transaction.domain.entity.RecurringTransaction;
import com.personalFinance.personal_finance.investment.domain.entity.Investment;
import com.personalFinance.personal_finance.user.domain.entity.User;

import jakarta.persistence.*;
import lombok.*;

import java.math.BigDecimal;
import java.time.LocalDate;

@Entity
@Table(name = "transactions")
@Getter
@NoArgsConstructor(access = AccessLevel.PROTECTED)
@AllArgsConstructor(access = AccessLevel.PRIVATE)
@Builder(access = AccessLevel.PRIVATE)
@EqualsAndHashCode(onlyExplicitlyIncluded = true)
@ToString(of = { "id", "descricao", "valor", "tipo" })
public class Transaction {

    // -------------------- IDENTIFICADOR --------------------

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @EqualsAndHashCode.Include
    private Long id;

    // -------------------- RELACIONAMENTOS --------------------

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "usuario_id", nullable = false)
    private User usuario;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "categoria_id", nullable = false)
    private Category categoria;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "transacao_recorrente_id")
    private RecurringTransaction transacaoRecorrente;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "investimento_id")
    private Investment investimento;

    // -------------------- CAMPOS EM PORTUGUÊS --------------------

    @Column(nullable = false, precision = 15, scale = 2)
    private BigDecimal valor;

    @Column(nullable = false)
    private LocalDate data;

    @Column(nullable = false, length = 300)
    private String descricao;

    @Enumerated(EnumType.STRING)
    @Column(nullable = false, length = 20)
    private TipoTransacao tipo;

    @Enumerated(EnumType.STRING)
    @Column(name = "metodo_pagamento", nullable = false, length = 30)
    private MetodoPagamento metodoPagamento;

    @Column(length = 1000)
    private String observacoes;

    @Column(name = "data_criacao", nullable = false, updatable = false)
    private LocalDate dataCriacao;

    // -------------------- FACTORY METHOD --------------------

    public static Transaction create(
            User usuario,
            Category categoria,
            BigDecimal valor,
            LocalDate data,
            String descricao,
            TipoTransacao tipo,
            MetodoPagamento metodoPagamento,
            String observacoes) {
        return Transaction.builder()
                .usuario(usuario)
                .categoria(categoria)
                .valor(valor)
                .data(data)
                .descricao(descricao)
                .tipo(tipo)
                .metodoPagamento(metodoPagamento)
                .observacoes(observacoes)
                .dataCriacao(LocalDate.now())
                .build();
    }

    // -------------------- MÉTODOS DE DOMÍNIO --------------------

    public void updateValor(BigDecimal novoValor) {
        this.valor = novoValor;
    }

    public void updateData(LocalDate novaData) {
        this.data = novaData;
    }

    public void updateDescricao(String novaDescricao) {
        this.descricao = novaDescricao;
    }

    public void updateCategoria(Category novaCategoria) {
        this.categoria = novaCategoria;
    }

    public void updateMetodoPagamento(MetodoPagamento novoMetodo) {
        this.metodoPagamento = novoMetodo;
    }

    public void updateObservacoes(String novasObs) {
        this.observacoes = novasObs;
    }

    public void linkRecurring(RecurringTransaction recurring) {
        this.transacaoRecorrente = recurring;
    }

    public void linkInvestment(Investment investimento) {
        this.investimento = investimento;
    }

    public boolean belongsToUser(Long usuarioId) {
        return this.usuario != null && this.usuario.getId().equals(usuarioId);
    }

    public void updateTipo(TipoTransacao novoTipo) {
        this.tipo = novoTipo;
    }
}
