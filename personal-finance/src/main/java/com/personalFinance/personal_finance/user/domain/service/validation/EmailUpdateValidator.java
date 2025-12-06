package com.personalFinance.personal_finance.user.domain.service.validation;

import com.personalFinance.personal_finance.shared.validator.BiValidator;
import com.personalFinance.personal_finance.shared.validator.ValidationResult;
import com.personalFinance.personal_finance.user.domain.entity.User;
import com.personalFinance.personal_finance.user.domain.port.UserExistencePort;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

/**
 * Validador especializado para atualização de email.
 * Verifica se o novo email é válido considerando o usuário atual.
 */
@Service
@RequiredArgsConstructor
@Slf4j
public class EmailUpdateValidator implements BiValidator<User, String> {

    private final UserExistencePort userExistencePort;
    private final EmailNormalizer emailNormalizer;

    @Override
    public ValidationResult validate(User currentUser, String newEmail) {
        // Normaliza o e-mail
        String normalizedEmail = emailNormalizer.normalize(newEmail);

        // Se for o mesmo do atual, é válido
        if (currentUser.getEmail().equals(normalizedEmail)) {
            return ValidationResult.valid();
        }

        // Se já existir outro usuário com esse e-mail, inválido
        if (userExistencePort.existsByEmail(normalizedEmail)) {
            log.warn("Tentativa de atualização com e-mail já existente: {}", normalizedEmail);
            return ValidationResult.invalid("E-mail já está em uso: " + normalizedEmail);
        }

        // Caso tudo esteja ok
        return ValidationResult.valid();
    }
}
