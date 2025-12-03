package com.personalFinance.personal_finance.investment.api.dto.request;

import jakarta.validation.constraints.DecimalMin;
import jakarta.validation.constraints.Size;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.math.BigDecimal;

/**
 * DTO para atualização de investimento.
 */
@Data
@NoArgsConstructor
@AllArgsConstructor
public class InvestmentUpdateRequestDTO {

    @DecimalMin(value = "0.00000001", message = "Quantidade deve ser maior que zero")
    private BigDecimal quantidade;

    @DecimalMin(value = "0.00", message = "Valor de compra não pode ser negativo")
    private BigDecimal valorCompra;

    @DecimalMin(value = "0.00", message = "Valor total investido não pode ser negativo")
    private BigDecimal valorTotalInvestido;

    @Size(max = 1000, message = "Observações não podem ter mais de 1000 caracteres")
    private String observacoes;

    private Boolean ativo;
}
