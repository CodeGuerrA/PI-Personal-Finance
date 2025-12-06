package com.personalFinance.personal_finance.transaction.domain.service.validation;

import com.personalFinance.personal_finance.shared.validator.ValidationResult;
import com.personalFinance.personal_finance.shared.validator.Validator;
import com.personalFinance.personal_finance.transaction.domain.entity.FrequenciaRecorrencia;
import org.springframework.stereotype.Component;

@Component
public class RecurringFrequenciaValidator implements Validator<FrequenciaRecorrencia> {

    @Override
    public ValidationResult validate(FrequenciaRecorrencia freq) {
        if (freq == null) {
            return ValidationResult.invalid("Frequência não pode ser nula");
        }
        return ValidationResult.valid();
    }
}