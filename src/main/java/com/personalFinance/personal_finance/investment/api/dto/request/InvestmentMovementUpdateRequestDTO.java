package com.personalFinance.personal_finance.investment.api.dto.request;

import jakarta.validation.constraints.*;
import lombok.*;

import java.math.BigDecimal;
import java.time.LocalDate;

/**
 * DTO para atualização de movimentação de investimento.
 * Apenas campos que podem ser alterados.
 */
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class InvestmentMovementUpdateRequestDTO {

    @DecimalMin(value = "0.0", inclusive = false, message = "A quantidade deve ser maior que zero")
    private BigDecimal quantidade;

    @DecimalMin(value = "0.0", inclusive = false, message = "O valor unitário deve ser maior que zero")
    private BigDecimal valorUnitario;

    @DecimalMin(value = "0.0", inclusive = false, message = "O valor total deve ser maior que zero")
    private BigDecimal valorTotal;

    @DecimalMin(value = "0.0", message = "As taxas não podem ser negativas")
    private BigDecimal taxas;

    @Size(max = 1000, message = "As observações não podem exceder 1000 caracteres")
    private String observacoes;
}
