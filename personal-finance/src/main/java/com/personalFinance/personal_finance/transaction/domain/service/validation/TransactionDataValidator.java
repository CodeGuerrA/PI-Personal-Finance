package com.personalFinance.personal_finance.transaction.domain.service.validation;

import com.personalFinance.personal_finance.shared.validator.ValidationResult;
import com.personalFinance.personal_finance.shared.validator.Validator;

import org.springframework.stereotype.Component;

import java.time.LocalDate;

/**
 * Validador da data da transação.
 */
@Component
public class TransactionDataValidator implements Validator<LocalDate> {

    @Override
    public ValidationResult validate(LocalDate data) {
        if (data == null) {
            return ValidationResult.invalid("Data não pode ser nula");
        }

        if (data.isAfter(LocalDate.now())) {
            return ValidationResult.invalid("Data não pode ser futura");
        }

        return ValidationResult.valid();
    }
}
