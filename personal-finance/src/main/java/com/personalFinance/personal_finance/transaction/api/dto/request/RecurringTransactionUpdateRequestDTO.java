package com.personalFinance.personal_finance.transaction.api.dto.request;

import com.personalFinance.personal_finance.transaction.domain.entity.FrequenciaRecorrencia;
import com.personalFinance.personal_finance.transaction.domain.entity.TipoTransacao;

import jakarta.validation.constraints.DecimalMin;
import jakarta.validation.constraints.Size;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.math.BigDecimal;
import java.time.LocalDate;

/**
 * DTO para atualização de transação recorrente.
 */
@Data
@NoArgsConstructor
@AllArgsConstructor
public class RecurringTransactionUpdateRequestDTO {

    @DecimalMin(value = "0.01", message = "O valor deve ser maior que zero")
    private BigDecimal valor;

    private TipoTransacao tipo;

    private Long categoriaId;

    @Size(min = 2, max = 200, message = "A descrição deve ter entre 2 e 200 caracteres")
    private String descricao;

    private FrequenciaRecorrencia frequencia;

    private LocalDate dataInicio;

    private LocalDate dataFim;

    private Integer diaVencimento;

    private Boolean ativa;

    @Size(max = 1000, message = "As observações podem ter no máximo 1000 caracteres")
    private String observacoes;
}