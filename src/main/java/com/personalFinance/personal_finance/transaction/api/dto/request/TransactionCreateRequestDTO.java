package com.personalFinance.personal_finance.transaction.api.dto.request;

import com.personalFinance.personal_finance.transaction.domain.entity.MetodoPagamento;
import com.personalFinance.personal_finance.transaction.domain.entity.TipoTransacao;

import jakarta.validation.constraints.*;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.math.BigDecimal;
import java.time.LocalDate;

/**
 * DTO para criação de transação.
 */
@Data
@NoArgsConstructor
@AllArgsConstructor
public class TransactionCreateRequestDTO {

    @NotNull(message = "O valor é obrigatório")
    @DecimalMin(value = "0.01", message = "O valor deve ser maior que zero")
    private BigDecimal valor;

    @NotNull(message = "O tipo da transação é obrigatório")
    private TipoTransacao tipo;

    @NotNull(message = "O método de pagamento é obrigatório")
    private MetodoPagamento metodoPagamento;

    @NotNull(message = "A categoria é obrigatória")
    private Long categoriaId;

    @NotBlank(message = "A descrição é obrigatória")
    @Size(min = 2, max = 200, message = "A descrição deve ter entre 2 e 200 caracteres")
    private String descricao;

    @NotNull(message = "A data da transação é obrigatória")
    @PastOrPresent(message = "A data não pode ser futura")
    private LocalDate data;

    @Size(max = 1000, message = "As observações não podem ter mais de 1000 caracteres")
    private String observacoes;
}