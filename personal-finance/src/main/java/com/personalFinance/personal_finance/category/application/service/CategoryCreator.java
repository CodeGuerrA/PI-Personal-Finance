package com.personalFinance.personal_finance.category.application.service;

import com.personalFinance.personal_finance.category.api.dto.request.CategoryCreateRequestDTO;
import com.personalFinance.personal_finance.category.api.dto.response.CategoryResponseDTO;
import com.personalFinance.personal_finance.category.api.mapper.CategoryMapper;
import com.personalFinance.personal_finance.category.domain.entity.Category;
import com.personalFinance.personal_finance.category.domain.port.CategoryRepositoryPort;
import com.personalFinance.personal_finance.category.domain.service.CategoryValidationService;
import com.personalFinance.personal_finance.user.domain.entity.User;
import com.personalFinance.personal_finance.user.infrastructure.repository.UserRepository;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;

import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

/**
 * Application Service — CRIA categoria
 */
@Slf4j
@Service
@RequiredArgsConstructor
public class CategoryCreator {

    private final CategoryRepositoryPort categoryRepository;
    private final UserRepository userRepository;
    private final CategoryValidationService validationService;
    private final CategoryMapper mapper;

    @Transactional
    public CategoryResponseDTO create(String keycloakId, CategoryCreateRequestDTO dto) {

        log.info("Criando categoria para usuário {}", keycloakId);

        // 1 - Buscar usuário
        User user = userRepository.findByKeycloakId(keycloakId)
                .orElseThrow(() -> new IllegalArgumentException("Usuário não encontrado"));

        // 2 - Validar dados usando Domain Service (agora com os 4 parâmetros)
        validationService.validateCreation(
                dto.getNome(),
                dto.getCor(),
                dto.getTipo(),
                dto.getIcone()
        );

        // 3 - Verificar se o usuário já possui categoria com o mesmo nome
        if (categoryRepository.existsByUsuarioIdAndNome(user.getId(), dto.getNome())) {
            throw new IllegalArgumentException("Já existe categoria com este nome");
        }

        // 4 - Criar categoria via Factory do Rich Domain Model
        Category category = Category.createUserCategory(
                user,
                dto.getNome(),
                dto.getCor(),
                dto.getTipo(),
                dto.getIcone()
        );

        // 5 - Persistir
        Category saved = categoryRepository.save(category);

        // 6 - Retornar DTO
        return mapper.toResponseDTO(saved);
    }
}