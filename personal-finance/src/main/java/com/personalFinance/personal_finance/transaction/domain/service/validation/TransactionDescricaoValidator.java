package com.personalFinance.personal_finance.transaction.domain.service.validation;

import com.personalFinance.personal_finance.shared.validator.ValidationResult;
import com.personalFinance.personal_finance.shared.validator.Validator;
import org.springframework.stereotype.Component;

/**
 * Validador de descrição da transação.
 */
@Component
public class TransactionDescricaoValidator implements Validator<String> {

    private static final int MAX_LENGTH = 300;
    private static final int MIN_LENGTH = 2;

    @Override
    public ValidationResult validate(String descricao) {
        if (descricao == null || descricao.isBlank()) {
            return ValidationResult.invalid("Descrição não pode ser vazia");
        }

        if (descricao.length() < MIN_LENGTH) {
            return ValidationResult.invalid("Descrição deve ter pelo menos " + MIN_LENGTH + " caracteres");
        }

        if (descricao.length() > MAX_LENGTH) {
            return ValidationResult.invalid("Descrição deve ter no máximo " + MAX_LENGTH + " caracteres");
        }

        return ValidationResult.valid();
    }
}