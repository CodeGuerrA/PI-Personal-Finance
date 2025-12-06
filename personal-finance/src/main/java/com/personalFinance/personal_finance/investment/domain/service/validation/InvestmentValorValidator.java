package com.personalFinance.personal_finance.investment.domain.service.validation;

import com.personalFinance.personal_finance.shared.validator.ValidationResult;
import com.personalFinance.personal_finance.shared.validator.Validator;
import org.springframework.stereotype.Component;

import java.math.BigDecimal;

/**
 * Validador de valores do investimento seguindo SRP.
 */
@Component
public class InvestmentValorValidator implements Validator<BigDecimal> {

    @Override
    public ValidationResult validate(BigDecimal valor) {
        if (valor == null) {
            return ValidationResult.invalid("Valor não pode ser nulo");
        }

        if (valor.compareTo(BigDecimal.ZERO) < 0) {
            return ValidationResult.invalid("Valor não pode ser negativo");
        }

        return ValidationResult.valid();
    }
}
