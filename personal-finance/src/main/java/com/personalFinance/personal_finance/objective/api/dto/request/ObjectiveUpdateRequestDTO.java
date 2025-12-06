package com.personalFinance.personal_finance.objective.api.dto.request;

import jakarta.validation.constraints.*;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.math.BigDecimal;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class ObjectiveUpdateRequestDTO {

    @NotBlank(message = "Descrição é obrigatória")
    @Size(max = 500, message = "Descrição não pode ter mais de 500 caracteres")
    private String descricao;

    @NotNull(message = "Valor objetivo é obrigatório")
    @DecimalMin(value = "0.01", message = "Valor objetivo deve ser maior que zero")
    private BigDecimal valorObjetivo;

    @NotNull(message = "Valor atual é obrigatório")
    @DecimalMin(value = "0.00", message = "Valor atual não pode ser negativo")
    private BigDecimal valorAtual;
}
