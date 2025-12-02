package com.personalFinance.personal_finance.category.infrastructure.persistence;

import com.personalFinance.personal_finance.category.domain.entity.Category;
import com.personalFinance.personal_finance.category.domain.entity.CategoryType;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface CategoryRepository extends JpaRepository<Category, Long> {

    List<Category> findByUsuarioIdAndAtivaTrue(Long usuarioId);

    List<Category> findByPadraoTrueAndAtivaTrue();

    List<Category> findByUsuarioIdAndTipoAndAtivaTrue(Long usuarioId, CategoryType tipo);

    List<Category> findByPadraoTrueAndTipoAndAtivaTrue(CategoryType tipo);
}
