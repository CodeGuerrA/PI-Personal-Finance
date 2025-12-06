package com.personalFinance.personal_finance.category.api.controller;

import com.personalFinance.personal_finance.category.api.dto.request.CategoryCreateRequestDTO;
import com.personalFinance.personal_finance.category.api.dto.request.CategoryUpdateRequestDTO;
import com.personalFinance.personal_finance.category.api.dto.response.CategoryResponseDTO;
import com.personalFinance.personal_finance.category.application.facade.CategoryFacade;
import com.personalFinance.personal_finance.category.domain.entity.CategoryType;
import com.personalFinance.personal_finance.shared.security.AuthenticatedUserProvider;

import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@Slf4j
@RestController
@RequestMapping("/categories")
@RequiredArgsConstructor
public class CategoryController {

    private final CategoryFacade categoryFacade;
    private final AuthenticatedUserProvider authenticatedUserProvider;

    // ============================================================================
    // CREATE
    // ============================================================================
    @PostMapping
    public ResponseEntity<CategoryResponseDTO> createCategory(
            @Valid @RequestBody CategoryCreateRequestDTO dto) {

        String keycloakId = authenticatedUserProvider.getAuthenticatedKeycloakId();
        log.info("Requisição para criar categoria. Usuário: {}", keycloakId);

        CategoryResponseDTO response = categoryFacade.createCategory(keycloakId, dto);

        return ResponseEntity.status(HttpStatus.CREATED).body(response);
    }

    // ============================================================================
    // FIND BY ID
    // ============================================================================
    @GetMapping("/{id}")
    public ResponseEntity<CategoryResponseDTO> getCategoryById(@PathVariable Long id) {

        String keycloakId = authenticatedUserProvider.getAuthenticatedKeycloakId();
        log.info("Requisição para buscar categoria ID: {}. Usuário: {}", id, keycloakId);

        CategoryResponseDTO response = categoryFacade.findById(keycloakId, id);

        return ResponseEntity.ok(response);
    }

    // ============================================================================
    // LISTAR TODAS DISPONÍVEIS PARA O USUÁRIO (padrão + personalizadas)
    // ============================================================================
    @GetMapping
    public ResponseEntity<List<CategoryResponseDTO>> getAllCategoriesForUser() {

        String keycloakId = authenticatedUserProvider.getAuthenticatedKeycloakId();
        log.info("Requisição para listar categorias do usuário {}", keycloakId);

        List<CategoryResponseDTO> response = categoryFacade.findAllAvailableForUser(keycloakId);

        return ResponseEntity.ok(response);
    }

    // ============================================================================
    // FILTRAR POR TIPO (RECEITA ou DESPESA)
    // ============================================================================
    @GetMapping("/tipo/{tipo}")
    public ResponseEntity<List<CategoryResponseDTO>> getCategoriesByTipo(
            @PathVariable CategoryType tipo) {

        String keycloakId = authenticatedUserProvider.getAuthenticatedKeycloakId();
        log.info("Requisição para categorias tipo {} do usuário {}", tipo, keycloakId);

        List<CategoryResponseDTO> response = categoryFacade.findByUserAndTipo(keycloakId, tipo);

        return ResponseEntity.ok(response);
    }

    // ============================================================================
    // LISTAR ORDENADO POR NOME
    // ============================================================================
    @GetMapping("/ordenar/nome")
    public ResponseEntity<List<CategoryResponseDTO>> getCategoriesOrderByNome() {

        String keycloakId = authenticatedUserProvider.getAuthenticatedKeycloakId();
        log.info("Requisição para categorias ordenadas por nome. Usuário: {}", keycloakId);

        List<CategoryResponseDTO> response = categoryFacade.findByUserOrderByNome(keycloakId);

        return ResponseEntity.ok(response);
    }

    // ============================================================================
    // BUSCA PARCIAL POR NOME
    // ============================================================================
    @GetMapping("/buscar")
    public ResponseEntity<List<CategoryResponseDTO>> searchCategoriesByName(
            @RequestParam String nome) {

        String keycloakId = authenticatedUserProvider.getAuthenticatedKeycloakId();
        log.info("Requisição para buscar categorias contendo '{}'. Usuário: {}", nome, keycloakId);

        List<CategoryResponseDTO> response = categoryFacade.searchByName(keycloakId, nome);

        return ResponseEntity.ok(response);
    }

    // ============================================================================
    // CONTAR CATEGORIAS DO USUÁRIO
    // ============================================================================
    @GetMapping("/count")
    public ResponseEntity<Long> countCategories() {

        String keycloakId = authenticatedUserProvider.getAuthenticatedKeycloakId();
        log.info("Requisição para contar categorias do usuário {}", keycloakId);

        Long count = categoryFacade.countUserCategories(keycloakId);

        return ResponseEntity.ok(count);
    }

    // ============================================================================
    // CATEGORIAS MAIS USADAS
    // ============================================================================
    @GetMapping("/mais-usadas")
    public ResponseEntity<List<Object[]>> getMostUsedCategories() {

        String keycloakId = authenticatedUserProvider.getAuthenticatedKeycloakId();
        log.info("Requisição para categorias mais usadas. Usuário: {}", keycloakId);

        List<Object[]> response = categoryFacade.findMostUsedCategories(keycloakId);

        return ResponseEntity.ok(response);
    }

    // ============================================================================
    // UPDATE
    // ============================================================================
    @PutMapping("/{id}")
    public ResponseEntity<CategoryResponseDTO> updateCategory(
            @PathVariable Long id,
            @Valid @RequestBody CategoryUpdateRequestDTO dto) {

        String keycloakId = authenticatedUserProvider.getAuthenticatedKeycloakId();
        log.info("Requisição para atualizar categoria {}. Usuário: {}", id, keycloakId);

        CategoryResponseDTO response = categoryFacade.updateCategory(keycloakId, id, dto);

        return ResponseEntity.ok(response);
    }

    // ============================================================================
    // DELETE (desativação)
    // ============================================================================
    @DeleteMapping("/{id}")
    public ResponseEntity<Void> deleteCategory(@PathVariable Long id) {

        String keycloakId = authenticatedUserProvider.getAuthenticatedKeycloakId();
        log.info("Requisição para deletar categoria {}. Usuário: {}", id, keycloakId);

        categoryFacade.deleteCategory(keycloakId, id);

        return ResponseEntity.noContent().build();
    }
}