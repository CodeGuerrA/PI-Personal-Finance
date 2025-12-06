package com.personalFinance.personal_finance.category.application.service;

import com.personalFinance.personal_finance.category.api.dto.response.CategoryResponseDTO;
import com.personalFinance.personal_finance.category.api.mapper.CategoryMapper;
import com.personalFinance.personal_finance.category.domain.entity.Category;
import com.personalFinance.personal_finance.category.domain.entity.CategoryType;
import com.personalFinance.personal_finance.category.domain.exception.CategoryNotFoundException;
import com.personalFinance.personal_finance.category.domain.port.CategoryRepositoryPort;
import com.personalFinance.personal_finance.user.domain.entity.User;
import com.personalFinance.personal_finance.user.infrastructure.repository.UserRepository;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;

import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@Slf4j
@Service
@RequiredArgsConstructor
public class CategoryFinder {

    private final CategoryRepositoryPort categoryRepository;
    private final UserRepository userRepository;
    private final CategoryMapper mapper;

    @Transactional(readOnly = true)
    public CategoryResponseDTO findById(String keycloakId, Long id) {

        log.info("Buscando categoria {} do usuário {}", id, keycloakId);

        User user = userRepository.findByKeycloakId(keycloakId)
                .orElseThrow(() -> new IllegalArgumentException("Usuário não encontrado"));

        Category category = categoryRepository.findById(id)
                .orElseThrow(() -> new CategoryNotFoundException(id));

        if (!category.pertenceAoUsuario(user)) {
            throw new IllegalArgumentException("Categoria não pertence ao usuário");
        }

        return mapper.toResponseDTO(category);
    }

    @Transactional(readOnly = true)
    public List<CategoryResponseDTO> findAllAvailableForUser(String keycloakId) {

        log.info("Buscando categorias disponíveis do usuário {}", keycloakId);

        User user = userRepository.findByKeycloakId(keycloakId)
                .orElseThrow(() -> new IllegalArgumentException("Usuário não encontrado"));

        List<Category> categories = categoryRepository.findAllAvailableForUser(user.getId());

        return mapper.toResponseDTOList(categories);
    }

    @Transactional(readOnly = true)
    public List<CategoryResponseDTO> findByUserAndTipo(String keycloakId, CategoryType tipo) {

        log.info("Buscando categorias tipo {} do usuário {}", tipo, keycloakId);

        User user = userRepository.findByKeycloakId(keycloakId)
                .orElseThrow(() -> new IllegalArgumentException("Usuário não encontrado"));

        List<Category> categories = categoryRepository.findAllAvailableForUserByTipo(user.getId(), tipo);

        return mapper.toResponseDTOList(categories);
    }

    @Transactional(readOnly = true)
    public List<CategoryResponseDTO> findByUserOrderByNome(String keycloakId) {

        log.info("Buscando categorias por nome do usuário {}", keycloakId);

        User user = userRepository.findByKeycloakId(keycloakId)
                .orElseThrow(() -> new IllegalArgumentException("Usuário não encontrado"));

        List<Category> categories = categoryRepository.findByUsuarioIdAndAtivaTrueOrderByNomeAsc(user.getId());

        return mapper.toResponseDTOList(categories);
    }

    @Transactional(readOnly = true)
    public List<CategoryResponseDTO> searchByName(String keycloakId, String nome) {

        log.info("Buscando categorias por nome parcial do usuário {}", keycloakId);

        User user = userRepository.findByKeycloakId(keycloakId)
                .orElseThrow(() -> new IllegalArgumentException("Usuário não encontrado"));

        List<Category> categories = categoryRepository.findAllAvailableForUserByNomeContaining(user.getId(), nome);

        return mapper.toResponseDTOList(categories);
    }

    @Transactional(readOnly = true)
    public Long countUserCategories(String keycloakId) {

        log.info("Contando categorias do usuário {}", keycloakId);

        User user = userRepository.findByKeycloakId(keycloakId)
                .orElseThrow(() -> new IllegalArgumentException("Usuário não encontrado"));

        return categoryRepository.countByUsuarioIdAndAtivaTrue(user.getId());
    }

    @Transactional(readOnly = true)
    public List<Object[]> findMostUsedCategories(String keycloakId) {

        log.info("Buscando categorias mais usadas do usuário {}", keycloakId);

        User user = userRepository.findByKeycloakId(keycloakId)
                .orElseThrow(() -> new IllegalArgumentException("Usuário não encontrado"));

        return categoryRepository.findMostUsedCategories(user.getId());
    }
}