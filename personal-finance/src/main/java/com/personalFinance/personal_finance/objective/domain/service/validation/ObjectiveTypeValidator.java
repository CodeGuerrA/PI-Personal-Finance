package com.personalFinance.personal_finance.objective.domain.service.validation;

import com.personalFinance.personal_finance.objective.domain.entity.ObjectiveType;
import com.personalFinance.personal_finance.shared.validator.BiValidator;
import com.personalFinance.personal_finance.shared.validator.ValidationResult;
import org.springframework.stereotype.Component;

/**
 * Validador de tipo de objetivo e categoria seguindo SRP.
 * Valida a regra: LIMITE_CATEGORIA requer categoriaId.
 */
@Component
public class ObjectiveTypeValidator implements BiValidator<ObjectiveType, Long> {

    @Override
    public ValidationResult validate(ObjectiveType tipo, Long categoriaId) {
        if (tipo == null) {
            return ValidationResult.invalid("Tipo de objetivo não pode ser nulo");
        }

        if (tipo == ObjectiveType.LIMITE_CATEGORIA && categoriaId == null) {
            return ValidationResult.invalid("Categoria é obrigatória para tipo LIMITE_CATEGORIA");
        }

        return ValidationResult.valid();
    }
}
