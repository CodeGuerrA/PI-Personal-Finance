package com.personalFinance.personal_finance.transaction.domain.entity;

import com.personalFinance.personal_finance.user.domain.entity.User;
import com.personalFinance.personal_finance.category.domain.entity.Category;

import jakarta.persistence.*;
import lombok.*;

import java.math.BigDecimal;
import java.time.LocalDate;

/**
 * Rich Domain Model: Entidade RecurringTransaction.
 * Representa uma transação repetida automaticamente em intervalos fixos.
 */
@Entity
@Table(name = "recurring_transactions")
@Getter
@NoArgsConstructor(access = AccessLevel.PROTECTED)
@AllArgsConstructor(access = AccessLevel.PRIVATE)
@Builder(access = AccessLevel.PRIVATE)
@EqualsAndHashCode(onlyExplicitlyIncluded = true)
@ToString(of = {"id", "descricao", "valor", "frequencia"})
public class RecurringTransaction {

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

    // -------------------- CAMPOS EM PORTUGUÊS --------------------

    @Column(nullable = false, precision = 15, scale = 2)
    private BigDecimal valor;

    @Column(nullable = false, length = 300)
    private String descricao;

    @Column(nullable = false)
    private LocalDate dataInicio;

    @Column
    private LocalDate dataFim;

    @Enumerated(EnumType.STRING)
    @Column(nullable = false, length = 20)
    private FrequenciaRecorrencia frequencia;

    @Enumerated(EnumType.STRING)
    @Column(nullable = false, length = 20)
    private TipoTransacao tipo;

    @Column(name = "dia_vencimento")
    private Integer diaVencimento;

    @Column(name = "proxima_data")
    private LocalDate proximaData;

    @Column(nullable = false)
    private Boolean ativa;

    @Column(length = 1000)
    private String observacoes;

    @Column(name = "data_criacao", nullable = false, updatable = false)
    private LocalDate dataCriacao;

    // -------------------- FACTORY METHOD --------------------

    public static RecurringTransaction create(
            User usuario,
            Category categoria,
            BigDecimal valor,
            String descricao,
            LocalDate dataInicio,
            FrequenciaRecorrencia frequencia,
            TipoTransacao tipo,
            Integer diaVencimento,
            String observacoes
    ) {
        LocalDate proximaData = calcularProximaData(dataInicio, frequencia, diaVencimento);

        return RecurringTransaction.builder()
                .usuario(usuario)
                .categoria(categoria)
                .valor(valor)
                .descricao(descricao)
                .dataInicio(dataInicio)
                .frequencia(frequencia)
                .tipo(tipo)
                .diaVencimento(diaVencimento)
                .proximaData(proximaData)
                .ativa(true)
                .observacoes(observacoes)
                .dataCriacao(LocalDate.now())
                .build();
    }

    // -------------------- MÉTODOS DE DOMÍNIO --------------------

    public void updateValor(BigDecimal novoValor) {
        this.valor = novoValor;
    }

    public void updateDescricao(String novaDescricao) {
        this.descricao = novaDescricao;
    }

    public void updateCategoria(Category novaCategoria) {
        this.categoria = novaCategoria;
    }

    public void updateFrequencia(FrequenciaRecorrencia novaFrequencia) {
        this.frequencia = novaFrequencia;
    }

    public void updateDataInicio(LocalDate novaDataInicio) {
        this.dataInicio = novaDataInicio;
    }

    public void updateDataFim(LocalDate novaDataFim) {
        this.dataFim = novaDataFim;
    }

    public void updateObservacoes(String novasObs) {
        this.observacoes = novasObs;
    }

    public void updateDiaVencimento(Integer novoDiaVencimento) {
        this.diaVencimento = novoDiaVencimento;
        this.proximaData = calcularProximaData(LocalDate.now(), this.frequencia, novoDiaVencimento);
    }

    public void ativar() {
        this.ativa = true;
    }

    public void desativar() {
        this.ativa = false;
    }

    public void avancarProximaData() {
        this.proximaData = calcularProximaData(this.proximaData, this.frequencia, this.diaVencimento);
    }

    public boolean belongsToUser(Long usuarioId) {
        return this.usuario != null && this.usuario.getId().equals(usuarioId);
    }

    public boolean isVencida() {
        return this.proximaData != null && this.proximaData.isBefore(LocalDate.now());
    }

    public boolean deveExecutarHoje() {
        return this.ativa && this.proximaData != null && !this.proximaData.isAfter(LocalDate.now());
    }

    // -------------------- MÉTODOS ESTÁTICOS DE CÁLCULO --------------------

    private static LocalDate calcularProximaData(LocalDate dataBase, FrequenciaRecorrencia frequencia, Integer diaVencimento) {
        if (dataBase == null || frequencia == null) {
            return null;
        }

        LocalDate proxima = dataBase;

        switch (frequencia) {
            case DIARIA:
                proxima = dataBase.plusDays(1);
                break;
            case SEMANAL:
                proxima = dataBase.plusWeeks(1);
                break;
            case MENSAL:
                proxima = calcularProximaDataMensal(dataBase, diaVencimento);
                break;
            case ANUAL:
                proxima = dataBase.plusYears(1);
                if (diaVencimento != null) {
                    proxima = proxima.withDayOfMonth(Math.min(diaVencimento, proxima.lengthOfMonth()));
                }
                break;
        }

        return proxima;
    }

    private static LocalDate calcularProximaDataMensal(LocalDate dataBase, Integer diaVencimento) {
        if (diaVencimento == null) {
            return dataBase.plusMonths(1);
        }

        LocalDate proximoMes = dataBase.plusMonths(1);
        int ultimoDiaDoMes = proximoMes.lengthOfMonth();
        int diaAjustado = Math.min(diaVencimento, ultimoDiaDoMes);

        return proximoMes.withDayOfMonth(diaAjustado);
    }
}