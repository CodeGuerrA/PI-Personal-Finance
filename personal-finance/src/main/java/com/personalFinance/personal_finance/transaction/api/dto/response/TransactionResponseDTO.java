package com.personalFinance.personal_finance.transaction.api.dto.response;

import com.personalFinance.personal_finance.transaction.domain.entity.MetodoPagamento;
import com.personalFinance.personal_finance.transaction.domain.entity.TipoTransacao;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.math.BigDecimal;
import java.time.LocalDate;

/**
 * DTO de resposta para transação.
 * Inclui todos os dados da transação.
 */
@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class TransactionResponseDTO {

    private Long id;
    private Long usuarioId;

    private BigDecimal valor;
    private TipoTransacao tipo;
    private MetodoPagamento metodoPagamento;

    private Long categoriaId;

    private String descricao;
    private LocalDate data;
    private String observacoes;

    private Long transacaoRecorrenteId;
    private Long investimentoId;
    private LocalDate dataCriacao;
}