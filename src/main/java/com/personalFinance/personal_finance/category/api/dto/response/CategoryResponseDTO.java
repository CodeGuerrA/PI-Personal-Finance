package com.personalFinance.personal_finance.category.api.dto.response;

import com.personalFinance.personal_finance.category.domain.entity.CategoryType;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

/**
 * DTO de resposta para categoria.
 */
@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class CategoryResponseDTO {

    private Long id;
    private Long usuarioId;
    private String nome;
    private String cor;
    private CategoryType tipo;
    private String icone;
    private Boolean padrao;
    private Boolean ativa;
}