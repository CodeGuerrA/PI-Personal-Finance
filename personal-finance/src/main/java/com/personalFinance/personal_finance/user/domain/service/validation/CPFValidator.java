package com.personalFinance.personal_finance.user.domain.service.validation;

import com.personalFinance.personal_finance.shared.validator.ValidationResult;
import com.personalFinance.personal_finance.shared.validator.Validator;
import com.personalFinance.personal_finance.user.domain.port.UserExistencePort;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

/**
 * Validador de CPF que verifica se o CPF já está cadastrado no sistema.
 */
@Service
@RequiredArgsConstructor
public class CPFValidator implements Validator<String> {
    private final UserExistencePort userExistencePort;

    @Override
    public ValidationResult validate(String value) {
        if (userExistencePort.existsByCpf(value)) {
            return ValidationResult.invalid("Ja existe uma conta cadastrada com esse CPF");
        }
        return ValidationResult.valid();
    }
}
