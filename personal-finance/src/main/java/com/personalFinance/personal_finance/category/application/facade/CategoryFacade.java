package com.personalFinance.personal_finance.category.application.facade;

import com.personalFinance.personal_finance.category.api.dto.request.CategoryCreateRequestDTO;
import com.personalFinance.personal_finance.category.api.dto.request.CategoryUpdateRequestDTO;
import com.personalFinance.personal_finance.category.api.dto.response.CategoryResponseDTO;
import com.personalFinance.personal_finance.category.domain.entity.CategoryType;

import java.util.List;

public interface CategoryFacade {

    CategoryResponseDTO createCategory(String keycloakId, CategoryCreateRequestDTO dto);

    CategoryResponseDTO updateCategory(String keycloakId, Long id, CategoryUpdateRequestDTO dto);

    void deleteCategory(String keycloakId, Long id);

    CategoryResponseDTO findById(String keycloakId, Long id);

    List<CategoryResponseDTO> findAllAvailableForUser(String keycloakId);

    List<CategoryResponseDTO> findByUserAndTipo(String keycloakId, CategoryType tipo);

    List<CategoryResponseDTO> findByUserOrderByNome(String keycloakId);

    List<CategoryResponseDTO> searchByName(String keycloakId, String nome);

    Long countUserCategories(String keycloakId);

    List<Object[]> findMostUsedCategories(String keycloakId);
}