package com.personalFinance.personal_finance.category.api.dto.request;

import jakarta.validation.constraints.Pattern;
import jakarta.validation.constraints.Size;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

/**
 * DTO para atualização de categoria do usuário.
 */
@Data
@NoArgsConstructor
@AllArgsConstructor
public class CategoryUpdateRequestDTO {

    @Size(min = 2, max = 100, message = "Nome deve ter entre 2 e 100 caracteres")
    private String nome;

    @Pattern(
            regexp = "^#[0-9A-Fa-f]{6}$",
            message = "Cor deve estar no formato hexadecimal (#RRGGBB)"
    )
    private String cor;

    @Size(max = 50, message = "Nome do ícone não pode ter mais de 50 caracteres")
    private String icone;

    private Boolean ativa; // permite ativar/desativar
}