package com.personalFinance.personal_finance.transaction.api.dto.request;

import com.personalFinance.personal_finance.transaction.domain.entity.FrequenciaRecorrencia;
import com.personalFinance.personal_finance.transaction.domain.entity.TipoTransacao;

import jakarta.validation.constraints.*;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.math.BigDecimal;
import java.time.LocalDate;

/**
 * DTO para criação de transação recorrente.
 */
@Data
@NoArgsConstructor
@AllArgsConstructor
public class RecurringTransactionCreateRequestDTO {

    @NotNull(message = "O valor é obrigatório")
    @DecimalMin(value = "0.01", message = "O valor deve ser maior que zero")
    private BigDecimal valor;

    @NotNull(message = "O tipo da transação é obrigatório")
    private TipoTransacao tipo;

    @NotNull(message = "A categoria é obrigatória")
    private Long categoriaId;

    @NotBlank(message = "A descrição é obrigatória")
    @Size(min = 2, max = 200, message = "A descrição deve ter entre 2 e 200 caracteres")
    private String descricao;

    @NotNull(message = "A data de início é obrigatória")
    @PastOrPresent(message = "A data de início não pode ser futura")
    private LocalDate dataInicio;

    @NotNull(message = "A frequência é obrigatória")
    private FrequenciaRecorrencia frequencia;

    @Min(value = 1, message = "O dia de vencimento deve ser entre 1 e 31")
    @Max(value = 31, message = "O dia de vencimento deve ser entre 1 e 31")
    private Integer diaVencimento;

    private LocalDate dataFim;

    @Size(max = 1000, message = "As observações podem ter no máximo 1000 caracteres")
    private String observacoes;
}