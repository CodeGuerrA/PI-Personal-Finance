package com.personalFinance.personal_finance.category.domain.service.validation;

import com.personalFinance.personal_finance.shared.validator.ValidationResult;
import com.personalFinance.personal_finance.shared.validator.Validator;
import org.springframework.stereotype.Component;

@Component
public class CategoryNomeValidator implements Validator<String> {

    private static final int MAX = 100;
    private static final int MIN = 1;

    @Override
    public ValidationResult validate(String nome) {
        if (nome == null || nome.isBlank()) {
            return ValidationResult.invalid("Nome da categoria não pode ser vazio");
        }

        if (nome.length() < MIN) {
            return ValidationResult.invalid("Nome deve ter ao menos 1 caractere");
        }

        if (nome.length() > MAX) {
            return ValidationResult.invalid("Nome deve ter no máximo 100 caracteres");
        }

        return ValidationResult.valid();
    }
}