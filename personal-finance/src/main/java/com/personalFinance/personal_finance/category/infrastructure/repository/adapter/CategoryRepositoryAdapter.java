package com.personalFinance.personal_finance.category.infrastructure.repository.adapter;

import com.personalFinance.personal_finance.category.domain.entity.Category;
import com.personalFinance.personal_finance.category.domain.entity.CategoryType;
import com.personalFinance.personal_finance.category.domain.port.CategoryRepositoryPort;
import com.personalFinance.personal_finance.category.infrastructure.persistence.CategoryRepository;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;

import java.util.List;
import java.util.Optional;

/**
 * Adapter que implementa o Port do repositório de categorias.
 * Segue Hexagonal Architecture + Adapter Pattern.
 * Conecta o domínio (Port) com a infraestrutura (JPA Repository).
 */
@Component
@RequiredArgsConstructor
public class CategoryRepositoryAdapter implements CategoryRepositoryPort {

    private final CategoryRepository categoryRepository;

    @Override
    public Category save(Category category) {
        return categoryRepository.save(category);
    }

    @Override
    public Optional<Category> findById(Long id) {
        return categoryRepository.findById(id);
    }

    @Override
    public void delete(Category category) {
        categoryRepository.delete(category);
    }

    @Override
    public boolean existsById(Long id) {
        return categoryRepository.existsById(id);
    }

    @Override
    public boolean existsByUsuarioIdAndNome(Long usuarioId, String nome) {
        return categoryRepository.existsByUsuarioIdAndNome(usuarioId, nome);
    }

    @Override
    public List<Category> findByUsuarioIdAndAtivaTrue(Long usuarioId) {
        return categoryRepository.findByUsuarioIdAndAtivaTrue(usuarioId);
    }

    @Override
    public List<Category> findByPadraoTrueAndAtivaTrue() {
        return categoryRepository.findByPadraoTrueAndAtivaTrue();
    }

    @Override
    public List<Category> findByUsuarioIdAndTipoAndAtivaTrue(Long usuarioId, CategoryType tipo) {
        return categoryRepository.findByUsuarioIdAndTipoAndAtivaTrue(usuarioId, tipo);
    }

    @Override
    public List<Category> findByPadraoTrueAndTipoAndAtivaTrue(CategoryType tipo) {
        return categoryRepository.findByPadraoTrueAndTipoAndAtivaTrue(tipo);
    }

    @Override
    public List<Category> findByUsuarioIdAndNomeContaining(Long usuarioId, String nome) {
        return categoryRepository.findByUsuarioIdAndNomeContaining(usuarioId, nome);
    }

    @Override
    public List<Category> findAllAvailableForUserByNomeContaining(Long usuarioId, String nome) {
        return categoryRepository.findAllAvailableForUserByNomeContaining(usuarioId, nome);
    }

    @Override
    public List<Category> findAllAvailableForUser(Long usuarioId) {
        return categoryRepository.findAllAvailableForUser(usuarioId);
    }

    @Override
    public List<Category> findAllAvailableForUserByTipo(Long usuarioId, CategoryType tipo) {
        return categoryRepository.findAllAvailableForUserByTipo(usuarioId, tipo);
    }

    @Override
    public Long countByUsuarioIdAndAtivaTrue(Long usuarioId) {
        return categoryRepository.countByUsuarioIdAndAtivaTrue(usuarioId);
    }

    @Override
    public List<Category> findByUsuarioIdAndAtivaTrueOrderByNomeAsc(Long usuarioId) {
        return categoryRepository.findByUsuarioIdAndAtivaTrueOrderByNomeAsc(usuarioId);
    }

    @Override
    public List<Object[]> findMostUsedCategories(Long usuarioId) {
        return categoryRepository.findMostUsedCategories(usuarioId);
    }
}