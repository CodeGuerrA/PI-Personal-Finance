package com.personalFinance.personal_finance.category.domain.service.validation;

import com.personalFinance.personal_finance.shared.validator.ValidationResult;
import com.personalFinance.personal_finance.shared.validator.Validator;

import org.springframework.stereotype.Component;

@Component
public class CategoryIconeValidator implements Validator<String> {

    @Override
    public ValidationResult validate(String icone) {
        // Ícone é opcional → válido se estiver vazio
        if (icone == null || icone.isBlank()) {
            return ValidationResult.valid();
        }

        if (icone.length() > 50) {
            return ValidationResult.invalid("Ícone deve ter no máximo 50 caracteres");
        }

        return ValidationResult.valid();
    }
}