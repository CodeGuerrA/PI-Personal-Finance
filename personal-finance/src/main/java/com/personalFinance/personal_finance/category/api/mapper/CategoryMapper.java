package com.personalFinance.personal_finance.category.api.mapper;

import com.personalFinance.personal_finance.category.api.dto.request.CategoryCreateRequestDTO;
import com.personalFinance.personal_finance.category.api.dto.request.CategoryUpdateRequestDTO;
import com.personalFinance.personal_finance.category.api.dto.response.CategoryResponseDTO;
import com.personalFinance.personal_finance.category.domain.entity.Category;
import com.personalFinance.personal_finance.category.domain.entity.CategoryType;
import com.personalFinance.personal_finance.user.domain.entity.User;
import org.springframework.stereotype.Component;

import java.util.List;
import java.util.stream.Collectors;

@Component
public class CategoryMapper {

    /**
     * Converte Category → CategoryResponseDTO.
     */
    public CategoryResponseDTO toResponseDTO(Category category) {
        return CategoryResponseDTO.builder()
                .id(category.getId())
                .nome(category.getNome())
                .cor(category.getCor())
                .icone(category.getIcone())
                .tipo(category.getTipo())
                .padrao(category.getPadrao())
                .ativa(category.getAtiva())
                .usuarioId(category.getUsuario() != null ? category.getUsuario().getId() : null)
                .build();
    }

    /**
     * Converte lista de Category → lista de CategoryResponseDTO.
     */
    public List<CategoryResponseDTO> toResponseDTOList(List<Category> categories) {
        return categories.stream()
                .map(this::toResponseDTO)
                .collect(Collectors.toList());
    }

    /**
     * Converte DTO de criação → entidade Category.
     * Usa o Factory Method do domínio.
     */
    public Category toEntity(CategoryCreateRequestDTO dto, User usuario) {
        return Category.createUserCategory(
                usuario,
                dto.getNome(),
                dto.getCor(),
                dto.getTipo(),
                dto.getIcone()
        );
    }

    /**
     * Atualiza entidade usando DTO de atualização.
     * Apenas campos permitidos são alterados.
     */
    public void applyUpdates(Category category, CategoryUpdateRequestDTO dto) {
        if (dto.getNome() != null || dto.getCor() != null || dto.getIcone() != null) {
            category.atualizar(
                    dto.getNome() != null ? dto.getNome() : category.getNome(),
                    dto.getCor() != null ? dto.getCor() : category.getCor(),
                    dto.getIcone() != null ? dto.getIcone() : category.getIcone()
            );
        }
    }
}