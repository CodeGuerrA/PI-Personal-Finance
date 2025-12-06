package com.personalFinance.personal_finance.category.application.facade;

import com.personalFinance.personal_finance.category.api.dto.request.CategoryCreateRequestDTO;
import com.personalFinance.personal_finance.category.api.dto.request.CategoryUpdateRequestDTO;
import com.personalFinance.personal_finance.category.api.dto.response.CategoryResponseDTO;
import com.personalFinance.personal_finance.category.application.service.CategoryCreator;
import com.personalFinance.personal_finance.category.application.service.CategoryDeleter;
import com.personalFinance.personal_finance.category.application.service.CategoryFinder;
import com.personalFinance.personal_finance.category.application.service.CategoryUpdater;
import com.personalFinance.personal_finance.category.domain.entity.CategoryType;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;

import org.springframework.stereotype.Component;

import java.util.List;

@Slf4j
@Component
@RequiredArgsConstructor
public class CategoryFacadeImpl implements CategoryFacade {

    private final CategoryCreator categoryCreator;
    private final CategoryUpdater categoryUpdater;
    private final CategoryDeleter categoryDeleter;
    private final CategoryFinder categoryFinder;

    @Override
    public CategoryResponseDTO createCategory(String keycloakId, CategoryCreateRequestDTO dto) {
        log.info("Facade: Criando categoria do usuário {}", keycloakId);
        return categoryCreator.create(keycloakId, dto);
    }

    @Override
    public CategoryResponseDTO updateCategory(String keycloakId, Long id, CategoryUpdateRequestDTO dto) {
        log.info("Facade: Atualizando categoria {} do usuário {}", id, keycloakId);
        return categoryUpdater.update(keycloakId, id, dto);
    }

    @Override
    public void deleteCategory(String keycloakId, Long id) {
        log.info("Facade: Deletando categoria {} do usuário {}", id, keycloakId);
        categoryDeleter.delete(keycloakId, id);
    }

    @Override
    public CategoryResponseDTO findById(String keycloakId, Long id) {
        log.info("Facade: Buscando categoria {} do usuário {}", id, keycloakId);
        return categoryFinder.findById(keycloakId, id);
    }

    @Override
    public List<CategoryResponseDTO> findAllAvailableForUser(String keycloakId) {
        log.info("Facade: Buscando todas categorias disponíveis do usuário {}", keycloakId);
        return categoryFinder.findAllAvailableForUser(keycloakId);
    }

    @Override
    public List<CategoryResponseDTO> findByUserAndTipo(String keycloakId, CategoryType tipo) {
        log.info("Facade: Buscando categorias tipo={} do usuário {}", tipo, keycloakId);
        return categoryFinder.findByUserAndTipo(keycloakId, tipo);
    }

    @Override
    public List<CategoryResponseDTO> findByUserOrderByNome(String keycloakId) {
        log.info("Facade: Buscando categorias ordenadas por nome do usuário {}", keycloakId);
        return categoryFinder.findByUserOrderByNome(keycloakId);
    }

    @Override
    public List<CategoryResponseDTO> searchByName(String keycloakId, String nome) {
        log.info("Facade: Buscando categorias com nome contendo '{}' do usuário {}", nome, keycloakId);
        return categoryFinder.searchByName(keycloakId, nome);
    }

    @Override
    public Long countUserCategories(String keycloakId) {
        log.info("Facade: Contando categorias do usuário {}", keycloakId);
        return categoryFinder.countUserCategories(keycloakId);
    }

    @Override
    public List<Object[]> findMostUsedCategories(String keycloakId) {
        log.info("Facade: Buscando categorias mais usadas do usuário {}", keycloakId);
        return categoryFinder.findMostUsedCategories(keycloakId);
    }
}