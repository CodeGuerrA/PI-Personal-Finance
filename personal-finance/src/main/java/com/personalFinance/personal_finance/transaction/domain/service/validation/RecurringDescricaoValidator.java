package com.personalFinance.personal_finance.transaction.domain.service.validation;

import com.personalFinance.personal_finance.shared.validator.ValidationResult;
import com.personalFinance.personal_finance.shared.validator.Validator;
import org.springframework.stereotype.Component;

@Component
public class RecurringDescricaoValidator implements Validator<String> {

    @Override
    public ValidationResult validate(String descricao) {
        if (descricao == null || descricao.isBlank()) {
            return ValidationResult.invalid("Descrição não pode ser vazia");
        }

        if (descricao.length() > 300) {
            return ValidationResult.invalid("Descrição não pode ter mais de 300 caracteres");
        }

        return ValidationResult.valid();
    }
}