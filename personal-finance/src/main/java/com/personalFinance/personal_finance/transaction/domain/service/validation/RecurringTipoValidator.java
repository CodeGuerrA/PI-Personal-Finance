package com.personalFinance.personal_finance.transaction.domain.service.validation;

import com.personalFinance.personal_finance.shared.validator.ValidationResult;
import com.personalFinance.personal_finance.shared.validator.Validator;
import com.personalFinance.personal_finance.transaction.domain.entity.TipoTransacao;
import org.springframework.stereotype.Component;

@Component
public class RecurringTipoValidator implements Validator<TipoTransacao> {

    @Override
    public ValidationResult validate(TipoTransacao tipo) {
        if (tipo == null) {
            return ValidationResult.invalid("Tipo de transação não pode ser nulo");
        }
        return ValidationResult.valid();
    }
}