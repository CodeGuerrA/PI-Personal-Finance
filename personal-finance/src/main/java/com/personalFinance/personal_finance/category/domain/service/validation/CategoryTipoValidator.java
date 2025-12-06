package com.personalFinance.personal_finance.category.domain.service.validation;

import com.personalFinance.personal_finance.category.domain.entity.CategoryType;
import com.personalFinance.personal_finance.shared.validator.ValidationResult;
import com.personalFinance.personal_finance.shared.validator.Validator;

import org.springframework.stereotype.Component;

@Component
public class CategoryTipoValidator implements Validator<CategoryType> {

    @Override
    public ValidationResult validate(CategoryType tipo) {
        if (tipo == null) {
            return ValidationResult.invalid("Tipo da categoria não pode ser nulo");
        }

        return ValidationResult.valid();
    }
}