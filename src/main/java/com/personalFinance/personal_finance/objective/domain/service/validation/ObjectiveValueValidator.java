package com.personalFinance.personal_finance.objective.domain.service.validation;

import com.personalFinance.personal_finance.shared.validator.ValidationResult;
import com.personalFinance.personal_finance.shared.validator.Validator;
import org.springframework.stereotype.Component;

import java.math.BigDecimal;

/**
 * Validador de valor objetivo seguindo SRP.
 */
@Component
public class ObjectiveValueValidator implements Validator<BigDecimal> {

    @Override
    public ValidationResult validate(BigDecimal valor) {
        if (valor == null) {
            return ValidationResult.invalid("Valor objetivo não pode ser nulo");
        }

        if (valor.compareTo(BigDecimal.ZERO) <= 0) {
            return ValidationResult.invalid("Valor objetivo deve ser maior que zero");
        }

        return ValidationResult.valid();
    }
}
