package com.personalFinance.personal_finance.investment.domain.service.validation;

import com.personalFinance.personal_finance.shared.validator.ValidationResult;
import com.personalFinance.personal_finance.shared.validator.Validator;
import org.springframework.stereotype.Component;

import java.math.BigDecimal;

/**
 * Validador de quantidade do investimento seguindo SRP.
 */
@Component
public class InvestmentQuantidadeValidator implements Validator<BigDecimal> {

    @Override
    public ValidationResult validate(BigDecimal quantidade) {
        if (quantidade == null) {
            return ValidationResult.invalid("Quantidade não pode ser nula");
        }

        if (quantidade.compareTo(BigDecimal.ZERO) <= 0) {
            return ValidationResult.invalid("Quantidade deve ser maior que zero");
        }

        return ValidationResult.valid();
    }
}
