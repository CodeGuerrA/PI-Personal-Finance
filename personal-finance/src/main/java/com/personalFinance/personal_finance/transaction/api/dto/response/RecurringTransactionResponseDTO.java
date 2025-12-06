package com.personalFinance.personal_finance.transaction.api.dto.response;

import com.personalFinance.personal_finance.transaction.domain.entity.FrequenciaRecorrencia;
import com.personalFinance.personal_finance.transaction.domain.entity.TipoTransacao;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.math.BigDecimal;
import java.time.LocalDate;

/**
 * DTO de resposta para transação recorrente.
 */
@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class RecurringTransactionResponseDTO {

    private Long id;
    private Long usuarioId;

    private BigDecimal valor;
    private TipoTransacao tipo;

    private Long categoriaId;

    private String descricao;

    private LocalDate dataInicio;
    private LocalDate dataFim;

    private FrequenciaRecorrencia frequencia;

    private Integer diaVencimento;
    private LocalDate proximaData;
    private Boolean ativa;

    private String observacoes;

    private LocalDate dataCriacao;
}