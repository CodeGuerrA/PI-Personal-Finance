package com.personalFinance.personal_finance.objective.domain.service.validation;

import com.personalFinance.personal_finance.shared.validator.ValidationResult;
import com.personalFinance.personal_finance.shared.validator.Validator;
import org.springframework.stereotype.Component;

/**
 * Validador de descrição de objetivo seguindo SRP.
 */
@Component
public class ObjectiveDescriptionValidator implements Validator<String> {

    private static final int MAX_LENGTH = 500;

    @Override
    public ValidationResult validate(String descricao) {
        if (descricao == null || descricao.isBlank()) {
            return ValidationResult.invalid("Descrição não pode ser nula ou vazia");
        }

        if (descricao.length() > MAX_LENGTH) {
            return ValidationResult.invalid("Descrição não pode ter mais de " + MAX_LENGTH + " caracteres");
        }

        return ValidationResult.valid();
    }
}
