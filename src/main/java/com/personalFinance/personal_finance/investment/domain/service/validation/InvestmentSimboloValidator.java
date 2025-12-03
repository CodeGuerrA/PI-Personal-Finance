package com.personalFinance.personal_finance.investment.domain.service.validation;

import com.personalFinance.personal_finance.shared.validator.ValidationResult;
import com.personalFinance.personal_finance.shared.validator.Validator;
import org.springframework.stereotype.Component;

/**
 * Validador de símbolo do ativo seguindo SRP.
 */
@Component
public class InvestmentSimboloValidator implements Validator<String> {

    private static final int MAX_LENGTH = 20;
    private static final int MIN_LENGTH = 1;

    @Override
    public ValidationResult validate(String simbolo) {
        if (simbolo == null || simbolo.isBlank()) {
            return ValidationResult.invalid("Símbolo do ativo não pode ser vazio");
        }

        if (simbolo.length() < MIN_LENGTH) {
            return ValidationResult.invalid("Símbolo do ativo deve ter pelo menos " + MIN_LENGTH + " caractere");
        }

        if (simbolo.length() > MAX_LENGTH) {
            return ValidationResult.invalid("Símbolo do ativo deve ter no máximo " + MAX_LENGTH + " caracteres");
        }

        return ValidationResult.valid();
    }
}
