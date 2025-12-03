package com.personalFinance.personal_finance.investment.api.dto.request;

import com.personalFinance.personal_finance.investment.domain.entity.TipoInvestimento;
import jakarta.validation.constraints.*;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.math.BigDecimal;
import java.time.LocalDate;

/**
 * DTO para criação de investimento.
 */
@Data
@NoArgsConstructor
@AllArgsConstructor
public class InvestmentCreateRequestDTO {

    @NotNull(message = "Tipo de investimento é obrigatório")
    private TipoInvestimento tipoInvestimento;

    @NotBlank(message = "Nome do ativo é obrigatório")
    @Size(min = 2, max = 200, message = "Nome do ativo deve ter entre 2 e 200 caracteres")
    private String nomeAtivo;

    @NotBlank(message = "Símbolo do ativo é obrigatório")
    @Size(min = 1, max = 20, message = "Símbolo deve ter entre 1 e 20 caracteres")
    private String simbolo;

    @NotNull(message = "Quantidade é obrigatória")
    @DecimalMin(value = "0.00000001", message = "Quantidade deve ser maior que zero")
    private BigDecimal quantidade;

    @NotNull(message = "Valor de compra é obrigatório")
    @DecimalMin(value = "0.00", message = "Valor de compra não pode ser negativo")
    private BigDecimal valorCompra;

    @NotNull(message = "Valor total investido é obrigatório")
    @DecimalMin(value = "0.00", message = "Valor total investido não pode ser negativo")
    private BigDecimal valorTotalInvestido;

    @NotNull(message = "Data de compra é obrigatória")
    @PastOrPresent(message = "Data de compra não pode ser futura")
    private LocalDate dataCompra;

    @Size(max = 100, message = "Nome da corretora não pode ter mais de 100 caracteres")
    private String corretora;

    @Size(max = 1000, message = "Observações não podem ter mais de 1000 caracteres")
    private String observacoes;
}
