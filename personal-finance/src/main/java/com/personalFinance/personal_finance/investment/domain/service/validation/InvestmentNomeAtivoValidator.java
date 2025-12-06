package com.personalFinance.personal_finance.investment.domain.service.validation;

import com.personalFinance.personal_finance.shared.validator.ValidationResult;
import com.personalFinance.personal_finance.shared.validator.Validator;
import org.springframework.stereotype.Component;

/**
 * Validador de nome do ativo seguindo SRP.
 */
@Component
public class InvestmentNomeAtivoValidator implements Validator<String> {

    private static final int MAX_LENGTH = 200;
    private static final int MIN_LENGTH = 2;

    @Override
    public ValidationResult validate(String nomeAtivo) {
        if (nomeAtivo == null || nomeAtivo.isBlank()) {
            return ValidationResult.invalid("Nome do ativo não pode ser vazio");
        }

        if (nomeAtivo.length() < MIN_LENGTH) {
            return ValidationResult.invalid("Nome do ativo deve ter pelo menos " + MIN_LENGTH + " caracteres");
        }

        if (nomeAtivo.length() > MAX_LENGTH) {
            return ValidationResult.invalid("Nome do ativo deve ter no máximo " + MAX_LENGTH + " caracteres");
        }

        return ValidationResult.valid();
    }
}
