package com.personalFinance.personal_finance.objective.api.dto.request;

import com.personalFinance.personal_finance.objective.domain.entity.ObjectiveType;
import jakarta.validation.constraints.*;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.math.BigDecimal;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class ObjectiveCreateRequestDTO {

    private Long categoriaId;

    @NotBlank(message = "Descrição é obrigatória")
    @Size(max = 500, message = "Descrição não pode ter mais de 500 caracteres")
    private String descricao;

    @NotNull(message = "Valor objetivo é obrigatório")
    @DecimalMin(value = "0.01", message = "Valor objetivo deve ser maior que zero")
    private BigDecimal valorObjetivo;

    @NotBlank(message = "Mês/Ano é obrigatório")
    @Pattern(regexp = "^\\d{4}-\\d{2}$", message = "Formato de Mês/Ano inválido. Use 'yyyy-MM' (Ex: '2024-12')")
    private String mesAno;

    @NotNull(message = "Tipo de objetivo é obrigatório")
    private ObjectiveType tipo;
}
