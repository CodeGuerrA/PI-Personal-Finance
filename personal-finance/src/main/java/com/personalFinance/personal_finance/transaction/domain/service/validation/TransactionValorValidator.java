package com.personalFinance.personal_finance.transaction.domain.service.validation;

import com.personalFinance.personal_finance.shared.validator.ValidationResult;
import com.personalFinance.personal_finance.shared.validator.Validator;

import org.springframework.stereotype.Component;

import java.math.BigDecimal;

/**
 * Validador de valor da transação.
 */
@Component
public class TransactionValorValidator implements Validator<BigDecimal> {

    @Override
    public ValidationResult validate(BigDecimal valor) {
        if (valor == null) {
            return ValidationResult.invalid("Valor não pode ser nulo");
        }

        if (valor.compareTo(BigDecimal.ZERO) == 0) {
            return ValidationResult.invalid("Valor não pode ser zero");
        }

        if (valor.abs().compareTo(BigDecimal.valueOf(1_000_000)) > 0) {
            return ValidationResult.invalid("Valor é muito grande");
        }

        return ValidationResult.valid();
    }
}