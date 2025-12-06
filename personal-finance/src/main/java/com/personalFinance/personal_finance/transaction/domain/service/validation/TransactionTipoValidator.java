package com.personalFinance.personal_finance.transaction.domain.service.validation;

import com.personalFinance.personal_finance.shared.validator.ValidationResult;
import com.personalFinance.personal_finance.shared.validator.Validator;

import org.springframework.stereotype.Component;

/**
 * Validador de tipo de transação (RECEITA ou DESPESA).
 */
@Component
public class TransactionTipoValidator implements Validator<String> {

    @Override
    public ValidationResult validate(String tipo) {
        if (tipo == null || tipo.isBlank()) {
            return ValidationResult.invalid("Tipo não pode ser vazio");
        }

        if (!tipo.equalsIgnoreCase("RECEITA") && !tipo.equalsIgnoreCase("DESPESA")) {
            return ValidationResult.invalid("Tipo deve ser RECEITA ou DESPESA");
        }

        return ValidationResult.valid();
    }
}