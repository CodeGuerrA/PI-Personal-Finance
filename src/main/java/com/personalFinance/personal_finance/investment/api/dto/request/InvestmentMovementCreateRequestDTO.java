package com.personalFinance.personal_finance.investment.api.dto.request;

import com.personalFinance.personal_finance.investment.domain.entity.TipoMovimentacao;
import jakarta.validation.constraints.*;
import lombok.*;

import java.math.BigDecimal;
import java.time.LocalDate;

/**
 * DTO para criação de movimentação de investimento.
 * Validações usando Bean Validation (JSR 303).
 */
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class InvestmentMovementCreateRequestDTO {

    @NotNull(message = "O ID do investimento é obrigatório")
    private Long investmentId;

    @NotNull(message = "O tipo de movimentação é obrigatório")
    private TipoMovimentacao tipoMovimentacao;

    @NotNull(message = "A quantidade é obrigatória")
    @DecimalMin(value = "0.0", inclusive = false, message = "A quantidade deve ser maior que zero")
    private BigDecimal quantidade;

    @NotNull(message = "O valor unitário é obrigatório")
    @DecimalMin(value = "0.0", inclusive = false, message = "O valor unitário deve ser maior que zero")
    private BigDecimal valorUnitario;

    @NotNull(message = "O valor total é obrigatório")
    @DecimalMin(value = "0.0", inclusive = false, message = "O valor total deve ser maior que zero")
    private BigDecimal valorTotal;

    @DecimalMin(value = "0.0", message = "As taxas não podem ser negativas")
    private BigDecimal taxas;

    @NotNull(message = "A data da movimentação é obrigatória")
    @PastOrPresent(message = "A data da movimentação não pode ser futura")
    private LocalDate dataMovimentacao;

    @Size(max = 1000, message = "As observações não podem exceder 1000 caracteres")
    private String observacoes;
}
