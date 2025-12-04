package com.personalFinance.personal_finance.category.domain.service.validation;

import com.personalFinance.personal_finance.shared.validator.ValidationResult;
import com.personalFinance.personal_finance.shared.validator.Validator;

import org.springframework.stereotype.Component;

@Component
public class CategoryCorValidator implements Validator<String> {

    @Override
    public ValidationResult validate(String cor) {
        if (cor == null || cor.isBlank()) {
            return ValidationResult.invalid("Cor não pode ser vazia");
        }

        if (!cor.matches("^#[0-9A-Fa-f]{6}$")) {
            return ValidationResult.invalid("Cor deve estar no formato hexadecimal (#RRGGBB)");
        }

        return ValidationResult.valid();
    }
}