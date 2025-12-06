package com.personalFinance.personal_finance.category.domain.service;

import com.personalFinance.personal_finance.category.domain.entity.Category;
import com.personalFinance.personal_finance.category.domain.exception.UnauthorizedCategoryAccessException;
import com.personalFinance.personal_finance.user.domain.entity.User;
import org.springframework.stereotype.Service;

/**
 * Domain Service para validar propriedade de categorias.
 * Categorias padrão são acessíveis a todos; personalizadas só ao dono.
 */
@Service
public class CategoryOwnershipService {

    public boolean belongsToUser(Category category, User user) {
        if (category == null || user == null) {
            return false;
        }

        // Categoria padrão pertence a todos
        if (Boolean.TRUE.equals(category.getPadrao())) {
            return true;
        }

        return category.getUsuario() != null &&
               category.getUsuario().getId().equals(user.getId());
    }

    public void validateOwnership(Category category, User user) {
        if (!belongsToUser(category, user)) {
            throw new UnauthorizedCategoryAccessException();
        }
    }
}
