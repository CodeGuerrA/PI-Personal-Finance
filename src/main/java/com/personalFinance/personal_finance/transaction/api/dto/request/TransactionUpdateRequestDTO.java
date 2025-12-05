package com.personalFinance.personal_finance.transaction.api.dto.request;

import com.personalFinance.personal_finance.transaction.domain.entity.MetodoPagamento;
import com.personalFinance.personal_finance.transaction.domain.entity.TipoTransacao;

import jakarta.validation.constraints.DecimalMin;
import jakarta.validation.constraints.Size;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.math.BigDecimal;
import java.time.LocalDate;

/**
 * DTO para atualização de transação.
 */
@Data
@NoArgsConstructor
@AllArgsConstructor
public class TransactionUpdateRequestDTO {

    @DecimalMin(value = "0.01", message = "O valor deve ser maior que zero")
    private BigDecimal valor;

    private TipoTransacao tipo;

    private MetodoPagamento metodoPagamento;

    private Long categoriaId;

    @Size(min = 2, max = 200, message = "A descrição deve ter entre 2 e 200 caracteres")
    private String descricao;

    private LocalDate data;

    @Size(max = 1000, message = "As observações não podem ter mais de 1000 caracteres")
    private String observacoes;
}