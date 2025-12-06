package com.personalFinance.personal_finance.category.application.service;

import com.personalFinance.personal_finance.category.api.dto.request.CategoryUpdateRequestDTO;
import com.personalFinance.personal_finance.category.api.dto.response.CategoryResponseDTO;
import com.personalFinance.personal_finance.category.api.mapper.CategoryMapper;
import com.personalFinance.personal_finance.category.domain.entity.Category;
import com.personalFinance.personal_finance.category.domain.exception.CategoryNotFoundException;
import com.personalFinance.personal_finance.category.domain.port.CategoryRepositoryPort;

import com.personalFinance.personal_finance.category.domain.service.CategoryValidationService;
import com.personalFinance.personal_finance.user.domain.entity.User;

import com.personalFinance.personal_finance.user.infrastructure.repository.UserRepository;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;

import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Slf4j
@Service
@RequiredArgsConstructor
public class CategoryUpdater {

    private final CategoryRepositoryPort categoryRepository;
    private final UserRepository userRepository;
    private final CategoryValidationService validationService;
    private final CategoryMapper mapper;

    @Transactional
    public CategoryResponseDTO update(String keycloakId, Long id, CategoryUpdateRequestDTO dto) {

        log.info("Atualizando categoria {} do usuário {}", id, keycloakId);

        User user = userRepository.findByKeycloakId(keycloakId)
                .orElseThrow(() -> new IllegalArgumentException("Usuário não encontrado"));

        Category category = categoryRepository.findById(id)
                .orElseThrow(() -> new CategoryNotFoundException(id));

        if (!category.pertenceAoUsuario(user)) {
            throw new IllegalArgumentException("Categoria não pertence ao usuário");
        }

        validationService.validateUpdate(dto.getNome(), dto.getCor(), dto.getIcone());

        if (dto.getNome() != null
                && categoryRepository.existsByUsuarioIdAndNome(user.getId(), dto.getNome())
                && !dto.getNome().equals(category.getNome())) {
            throw new IllegalArgumentException("Já existe categoria com esse nome");
        }

        category.atualizar(dto.getNome(), dto.getCor(), dto.getIcone());

        Category updated = categoryRepository.save(category);

        return mapper.toResponseDTO(updated);
    }
}