package com.personalFinance.personal_finance.user.domain.service.validation;

import com.personalFinance.personal_finance.shared.validator.ValidationResult;
import com.personalFinance.personal_finance.shared.validator.Validator;
import com.personalFinance.personal_finance.user.domain.port.UserExistencePort;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

/**
 * Validador de email que verifica se o email já está cadastrado no sistema.
 */
@Service
@RequiredArgsConstructor
public class EmailValidator implements Validator<String> {
    private final UserExistencePort userExistencePort;

    @Override
    public ValidationResult validate(String value) {
        if (userExistencePort.existsByEmail(value)) {
            return ValidationResult.invalid("Esse email ja esta cadastrado");
        }

        return ValidationResult.valid();
    }
}
