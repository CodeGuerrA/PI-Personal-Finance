package com.personalFinance.personal_finance.category.api.dto.request;

import com.personalFinance.personal_finance.category.domain.entity.CategoryType;
import jakarta.validation.constraints.*;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

/**
 * DTO para criação de categoria.
 */
@Data
@NoArgsConstructor
@AllArgsConstructor
public class CategoryCreateRequestDTO {

    @NotBlank(message = "Nome da categoria é obrigatório")
    @Size(min = 2, max = 100, message = "Nome deve ter entre 2 e 100 caracteres")
    private String nome;

    @NotBlank(message = "Cor é obrigatória")
    @Pattern(
            regexp = "^#[0-9A-Fa-f]{6}$",
            message = "Cor deve estar no formato hexadecimal (#RRGGBB)"
    )
    private String cor;

    @NotNull(message = "Tipo da categoria é obrigatório")
    private CategoryType tipo;

    @Size(max = 50, message = "Nome do ícone não pode ter mais de 50 caracteres")
    private String icone;
}
