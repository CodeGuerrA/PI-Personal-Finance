package com.personalFinance.personal_finance.transaction.domain.service.validation;

import com.personalFinance.personal_finance.shared.validator.ValidationResult;
import com.personalFinance.personal_finance.shared.validator.Validator;

import org.springframework.stereotype.Component;

/**
 * Validador do método de pagamento.
 */
@Component
public class TransactionMetodoPagamentoValidator implements Validator<String> {

    @Override
    public ValidationResult validate(String metodo) {
        if (metodo == null || metodo.isBlank()) {
            return ValidationResult.invalid("Método de pagamento não pode ser vazio");
        }

        return ValidationResult.valid();
    }
}
