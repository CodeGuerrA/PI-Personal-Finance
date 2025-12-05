package com.personalFinance.personal_finance.category.domain.port;

import com.personalFinance.personal_finance.category.domain.entity.Category;
import com.personalFinance.personal_finance.category.domain.entity.CategoryType;

import java.util.List;
import java.util.Optional;

public interface CategoryRepositoryPort {

    Category save(Category category);

    Optional<Category> findById(Long id);

    void delete(Category category);

    boolean existsById(Long id);

    // ---- CUSTOM METHODS ----

    boolean existsByUsuarioIdAndNome(Long usuarioId, String nome);

    List<Category> findByUsuarioIdAndAtivaTrue(Long usuarioId);

    List<Category> findByPadraoTrueAndAtivaTrue();

    List<Category> findByUsuarioIdAndTipoAndAtivaTrue(Long usuarioId, CategoryType tipo);

    List<Category> findByPadraoTrueAndTipoAndAtivaTrue(CategoryType tipo);

    List<Category> findByUsuarioIdAndNomeContaining(Long usuarioId, String nome);

    List<Category> findAllAvailableForUserByNomeContaining(Long usuarioId, String nome);

    List<Category> findAllAvailableForUser(Long usuarioId);

    List<Category> findAllAvailableForUserByTipo(Long usuarioId, CategoryType tipo);

    Long countByUsuarioIdAndAtivaTrue(Long usuarioId);

    List<Category> findByUsuarioIdAndAtivaTrueOrderByNomeAsc(Long usuarioId);

    List<Object[]> findMostUsedCategories(Long usuarioId);
}
