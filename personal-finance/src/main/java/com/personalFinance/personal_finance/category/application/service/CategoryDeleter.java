package com.personalFinance.personal_finance.category.application.service;

import com.personalFinance.personal_finance.category.domain.entity.Category;
import com.personalFinance.personal_finance.category.domain.exception.CategoryNotFoundException;
import com.personalFinance.personal_finance.category.domain.port.CategoryRepositoryPort;
import com.personalFinance.personal_finance.user.domain.entity.User;
import com.personalFinance.personal_finance.user.infrastructure.repository.UserRepository;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;

import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Slf4j
@Service
@RequiredArgsConstructor
public class CategoryDeleter {

    private final CategoryRepositoryPort categoryRepository;
    private final UserRepository userRepository;

    @Transactional
    public void delete(String keycloakId, Long id) {

        log.info("Deletando categoria {} do usuário {}", id, keycloakId);

        User user = userRepository.findByKeycloakId(keycloakId)
                .orElseThrow(() -> new IllegalArgumentException("Usuário não encontrado"));

        Category category = categoryRepository.findById(id)
                .orElseThrow(() -> new CategoryNotFoundException(id));

        if (!category.pertenceAoUsuario(user)) {
            throw new IllegalArgumentException("Categoria não pertence ao usuário");
        }

        category.desativar();

        categoryRepository.save(category);

        log.info("Categoria {} desativada com sucesso.", id);
    }
}